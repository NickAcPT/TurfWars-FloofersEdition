package io.github.nickacpt.replay.event

import io.github.nickacpt.replay.ReplayPlugin
import io.github.nickacpt.replay.platform.BukkitReplayPlatform
import io.github.nickacpt.replay.platform.ReplayItemStackUtils.controlType
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import java.util.*

object ReplayItemsListener : Listener {

    private var itemCooldownLastClicks = mutableMapOf<UUID, Long>()

    @EventHandler
    fun onItemInteractEvent(event: PlayerInteractEvent) {
        val controlType = event.item?.controlType ?: return

        if (event.hand == EquipmentSlot.HAND) {
            val viewer = BukkitReplayPlatform.convertIntoReplayViewer(event.player)
            val world = viewer.world ?: run {
                event.player.sendMessage(Component.text("No replay world?", NamedTextColor.RED))
                return
            }
            val session = world.session ?: run {
                event.player.sendMessage(Component.text("No replay session?", NamedTextColor.RED))
                return
            }

            event.setUseInteractedBlock(Event.Result.DENY)
            event.setUseItemInHand(Event.Result.DENY)

            val lastClick = itemCooldownLastClicks.getOrPut(event.player.uniqueId) { 0L }

            if (System.currentTimeMillis() - lastClick < 100) {
                return
            }

            itemCooldownLastClicks[event.player.uniqueId] = System.currentTimeMillis()

            ReplayPlugin.replaySystem.logic.onReplayControlItemInteraction(
                session,
                viewer,
                controlType,
                event.action.isRightClick
            )
        }
        event.isCancelled = true
    }

    @EventHandler
    fun onItemInteractEvent(event: PlayerDropItemEvent) {
        if (event.itemDrop.itemStack.controlType != null) {
            event.isCancelled = true
        }
    }

}
