package io.github.nickacpt.event.lobby.handlers

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent

object WorldEvents : Listener {
    @EventHandler
    fun onPlayerBreakBlock(e: BlockBreakEvent) {
        e.isCancelled = true
    }

    @EventHandler
    fun onPlayerPlaceBlock(e: BlockPlaceEvent) {
        e.isCancelled = true
    }
}