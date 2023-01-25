package io.github.nickacpt.event.turfwars.handlers

import io.github.nickacpt.event.core.display.events.TurfPlayerRefreshEvent
import io.github.nickacpt.event.utils.turfPlayer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

object PlayerEvents : Listener {

    @EventHandler
    fun onPlayerJoin(e: PlayerJoinEvent) {
        TurfPlayerRefreshEvent(e.player.turfPlayer).callEvent()
    }

}
