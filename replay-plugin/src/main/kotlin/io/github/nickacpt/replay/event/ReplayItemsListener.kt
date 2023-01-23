package io.github.nickacpt.replay.event

import io.github.nickacpt.replay.platform.ReplayItemStackUtils.controlType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent

object ReplayItemsListener : Listener {

    @EventHandler
    fun onItemInteractEvent(event: PlayerInteractEvent) {
        if (event.hasItem() && event.item?.controlType != null) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onItemInteractEvent(event: PlayerDropItemEvent) {
        if (event.itemDrop.itemStack.controlType != null) {
            event.isCancelled = true
        }
    }

}
