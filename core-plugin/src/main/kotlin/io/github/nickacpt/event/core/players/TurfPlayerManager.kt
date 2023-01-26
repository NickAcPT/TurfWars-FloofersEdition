package io.github.nickacpt.event.core.players

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.github.nickacpt.event.core.CorePlugin
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*
import java.util.concurrent.Semaphore

object TurfPlayerManager : Listener {

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
        val player = loadedPlayers.remove(e.player.uniqueId)
        if (player != null) {
            savePlayerData(player.uuid, player.data)
        }
    }

    private val playerLoadMutexMap = mutableMapOf<UUID, Semaphore>()
    private fun getMutex(uuid: UUID): Semaphore {
        return playerLoadMutexMap.getOrPut(uuid) { Semaphore(1) }
    }

    fun loadPlayerData(uuid: UUID): TurfPlayerData {
        CorePlugin.logger.info("Loading player data for $uuid")
        val playersPath = CorePlugin.instance.dataFolder.resolve("players")
        playersPath.mkdirs()

        val playerPath = playersPath.resolve("$uuid.json")
        if (!playerPath.exists()) {
            return TurfPlayerData(uuid)
        }

        return mapper.readValue(playerPath, TurfPlayerData::class.java)
    }

    private fun savePlayerData(uuid: UUID, data: TurfPlayerData) {
        CorePlugin.logger.info("Saving player data for $uuid")
        val playersPath = CorePlugin.instance.dataFolder.resolve("players")
        playersPath.mkdirs()

        mapper.writeValue(playersPath.resolve("$uuid.json"), data)
    }

    fun saveAll() {
        loadedPlayers.forEach { (uuid, player) ->
            savePlayerData(uuid, player.data)
        }
    }

    @EventHandler(priority = org.bukkit.event.EventPriority.LOWEST)
    fun onJoin(e: PlayerJoinEvent) {
        // Calling get will initialize the player instance if it doesn't exist
        getOrLoadTurfPlayer(e.player.uniqueId)
    }

    @EventHandler(priority = org.bukkit.event.EventPriority.MONITOR)
    fun onQuit(e: PlayerQuitEvent) {
        unloadTurfPlayer(e)
    }

}