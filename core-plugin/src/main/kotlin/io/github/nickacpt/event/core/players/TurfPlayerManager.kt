package io.github.nickacpt.event.core.players

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.github.nickacpt.event.core.CorePlugin
import io.github.nickacpt.event.core.io.DatabasePlayersFunctions
import io.github.nickacpt.event.utils.getDatabaseProxy
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*
import java.util.concurrent.Semaphore

object TurfPlayerManager : Listener {

    private val playersIo by lazy {
        getDatabaseProxy<DatabasePlayersFunctions>()
    }
    private val mapper = jacksonObjectMapper()
    private val loadedPlayers = mutableMapOf<UUID, TurfPlayer>()

    val players: Collection<TurfPlayer>
        get() = loadedPlayers.values

    private fun initializePlayerInstance(uuid: UUID) = TurfPlayer(uuid, loadPlayerData(uuid))

    fun getOrLoadTurfPlayer(uuid: UUID): TurfPlayer {
        getMutex(uuid).acquire()
        return loadedPlayers.getOrPut(uuid) { initializePlayerInstance(uuid) }.also {
            getMutex(uuid).release()
        }
    }

    private fun unloadTurfPlayer(e: PlayerQuitEvent) {
        val shouldForgetPlayer = CorePlugin.instance.config.forgetPlayersOnQuit
        val player = if (shouldForgetPlayer) {
            loadedPlayers.remove(e.player.uniqueId)
        } else {
            loadedPlayers[e.player.uniqueId]
        }

        if (player != null) {
            savePlayerData(player.uuid, player.data)
        }
    }

    private val playerLoadMutexMap = mutableMapOf<UUID, Semaphore>()
    private fun getMutex(uuid: UUID): Semaphore {
        return playerLoadMutexMap.getOrPut(uuid) { Semaphore(1) }
    }

    private fun loadPlayerData(uuid: UUID): TurfPlayerData {
        CorePlugin.logger.info("Loading player data for $uuid")
        return playersIo.getPlayerDataById(uuid)
    }

    private fun savePlayerData(uuid: UUID, data: TurfPlayerData) {
        CorePlugin.logger.info("Saving player data for $uuid")
        playersIo.updatePlayerData(uuid, data.currentTag)
    }

    fun saveAll() {
        loadedPlayers.forEach { (uuid, player) ->
            savePlayerData(uuid, player.data)
        }
    }

    @EventHandler(priority = org.bukkit.event.EventPriority.HIGHEST)
    fun onJoin(e: AsyncPlayerPreLoginEvent) {
        // Calling get will initialize the player instance if it doesn't exist
        getOrLoadTurfPlayer(e.uniqueId)
    }

    @EventHandler(priority = org.bukkit.event.EventPriority.MONITOR)
    fun onQuit(e: PlayerQuitEvent) {
        unloadTurfPlayer(e)
    }

}