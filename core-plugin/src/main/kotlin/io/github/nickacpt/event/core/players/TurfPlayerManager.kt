package io.github.nickacpt.event.core.players

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*

object TurfPlayerManager : Listener {

    private val loadedPlayers = mutableMapOf<UUID, TurfPlayer>()

    private fun initializePlayerInstance(uuid: UUID) = TurfPlayer(uuid)

    fun getTurfPlayer(uuid: UUID): TurfPlayer {
        return loadedPlayers.getOrPut(uuid) { initializePlayerInstance(uuid) }
    }

    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        // Calling get will initialize the player instance if it doesn't exist
        getTurfPlayer(e.player.uniqueId)
    }

    @EventHandler
    fun onQuit(e: PlayerQuitEvent) {
        loadedPlayers.remove(e.player.uniqueId)
    }

}