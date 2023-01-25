package io.github.nickacpt.event.turfwars.handlers

import io.github.nickacpt.event.turfwars.TurfWarsPlugin
import io.github.nickacpt.event.utils.turfPlayer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

object PlayerEvents : Listener {

    @EventHandler
    fun onPlayerJoin(e: PlayerJoinEvent) {
        val player = e.player.turfPlayer
        e.joinMessage(null)

        player.refresh()
        TurfWarsPlugin.testGame.addPlayer(player)
    }

    @EventHandler
    fun onPlayerQuit(e: PlayerQuitEvent) {
        e.quitMessage(null)
    }

}
