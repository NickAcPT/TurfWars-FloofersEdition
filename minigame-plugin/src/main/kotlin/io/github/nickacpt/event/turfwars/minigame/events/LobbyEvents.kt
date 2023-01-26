package io.github.nickacpt.event.turfwars.minigame.events

import io.github.nickacpt.event.turfwars.minigame.MinigameState
import io.github.nickacpt.event.turfwars.utils.cancelEvent
import io.papermc.paper.event.player.PrePlayerAttackEntityEvent
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageEvent

object LobbyEvents : Listener {

    @EventHandler
    fun onPlayerDamage(e: EntityDamageEvent) {
        if (e.entityType != EntityType.PLAYER || e.cause == EntityDamageEvent.DamageCause.VOID) return

        e.cancelEvent(*MinigameState.waitingForPlayersStates())
    }

    @EventHandler
    fun onPrePlayerAttack(e: PrePlayerAttackEntityEvent) {
        e.cancelEvent(*MinigameState.waitingForPlayersStates())
    }

    @EventHandler
    fun onPlayerBlockPlace(e: BlockPlaceEvent) {
        e.cancelEvent(*MinigameState.waitingForPlayersStates())
    }

    @EventHandler
    fun onPlayerBlockBreak(e: BlockBreakEvent) {
        e.cancelEvent(*MinigameState.waitingForPlayersStates())
    }

}