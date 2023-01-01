package io.github.nickacpt.event.core.players

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.github.nickacpt.event.core.CorePlugin
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*

object TurfPlayerManager : Listener {

    private val mapper = jacksonObjectMapper()
    private val loadedPlayers = mutableMapOf<UUID, TurfPlayer>()

    private fun initializePlayerInstance(uuid: UUID) = TurfPlayer(uuid, loadPlayerData(uuid))

    fun getOrLoadTurfPlayer(uuid: UUID): TurfPlayer {
        return loadedPlayers.getOrPut(uuid) { initializePlayerInstance(uuid) }
    }

    private fun unloadTurfPlayer(e: PlayerQuitEvent) {
        val player = loadedPlayers.remove(e.player.uniqueId)
        if (player != null) {
            savePlayerData(player.uuid, player.data)
        }
    }

    fun loadPlayerData(uuid: UUID): TurfPlayerData {
        CorePlugin.logger.info("Loading player data for $uuid")
        val playersPath = CorePlugin.instance.dataFolder.resolve("players")
        playersPath.mkdirs()

        return mapper.readValue(playersPath.resolve("$uuid.json"), TurfPlayerData::class.java)
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

    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        // Calling get will initialize the player instance if it doesn't exist
        getOrLoadTurfPlayer(e.player.uniqueId)
    }

    @EventHandler
    fun onQuit(e: PlayerQuitEvent) {
        unloadTurfPlayer(e)
    }

}