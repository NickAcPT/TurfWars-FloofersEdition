package io.github.nickacpt.event.core.players

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
    private val loadedPlayers = mutableMapOf<UUID, TurfPlayer>()

    val players: Collection<TurfPlayer>
        get() = loadedPlayers.values

    fun getOrLoadTurfPlayer(uuid: UUID, name: String? = null): TurfPlayer {
        getMutex(uuid).acquire()
        return loadedPlayers.getOrPut(uuid) { TurfPlayer(uuid, loadPlayerData(uuid, name)) }.also {
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

    private fun loadPlayerData(uuid: UUID, name: String? = null): TurfPlayerData {
        CorePlugin.logger.info("Loading player data for $uuid")
        return playersIo.getPlayerDataById(uuid, name)
    }

    private fun savePlayerData(uuid: UUID, data: TurfPlayerData) {
        CorePlugin.logger.info("Saving player data for $uuid")
        playersIo.updatePlayerData(uuid, data.name, data.currentTag)
    }

    fun saveAll() {
        loadedPlayers.forEach { (uuid, player) ->
            savePlayerData(uuid, player.data)
        }
    }

    @EventHandler(priority = org.bukkit.event.EventPriority.HIGHEST)
    fun onJoin(e: AsyncPlayerPreLoginEvent) {
        // Calling getOrLoadTurfPlayer will initialize the player instance if it doesn't exist
        getOrLoadTurfPlayer(e.uniqueId, e.name)
    }

    @EventHandler(priority = org.bukkit.event.EventPriority.MONITOR)
    fun onQuit(e: PlayerQuitEvent) {
        unloadTurfPlayer(e)
    }

}