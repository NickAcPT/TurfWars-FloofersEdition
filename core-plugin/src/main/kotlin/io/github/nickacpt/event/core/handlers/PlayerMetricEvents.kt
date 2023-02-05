package io.github.nickacpt.event.core.handlers

import io.github.nickacpt.event.core.metrics.PlayerMetricsManager
import io.github.nickacpt.event.utils.turfPlayer
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.player.PlayerAnimationEvent

object PlayerMetricEvents : Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    fun onArmSwing(e: PlayerAnimationEvent) {
        with(PlayerMetricsManager) {
            e.player.turfPlayer.incrementMetric { ARM_SWINGS }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    fun onBlockPlace(e: BlockBreakEvent) {
        with(PlayerMetricsManager) {
            e.player.turfPlayer.incrementMetric { BLOCKS_PLACED }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    fun onBlockBreak(e: BlockBreakEvent) {
        with(PlayerMetricsManager) {
            e.player.turfPlayer.incrementMetric { BLOCKS_BROKEN }
        }
    }

}