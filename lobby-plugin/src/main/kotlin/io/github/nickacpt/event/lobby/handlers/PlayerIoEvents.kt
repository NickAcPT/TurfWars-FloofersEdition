package io.github.nickacpt.event.lobby.handlers

import io.github.nickacpt.event.core.utils.resetPlayer
import io.github.nickacpt.event.lobby.LobbyPlugin
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

object PlayerIoEvents : Listener {
    @EventHandler
    fun onPlayerJoin(e: PlayerJoinEvent) {
        e.joinMessage(LobbyPlugin.instance.messages.playerJoinMessage(e.player))
        e.player.resetPlayer(teleportToSpawn = false, canFly = true)
    }

    @EventHandler
    fun onPlayerLeave(e: PlayerQuitEvent) {
        e.quitMessage(null)
    }
}