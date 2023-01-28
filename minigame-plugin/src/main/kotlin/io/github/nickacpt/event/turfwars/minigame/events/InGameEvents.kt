package io.github.nickacpt.event.turfwars.minigame.events

import io.github.nickacpt.event.turfwars.TurfWarsPlugin.Companion.locale
import io.github.nickacpt.event.turfwars.minigame.MinigameState
import io.github.nickacpt.event.turfwars.minigame.logic.TurfWarsLogic
import io.github.nickacpt.event.turfwars.utils.cancelEvent
import io.github.nickacpt.event.turfwars.utils.handleGameEvent
import io.papermc.paper.event.player.PrePlayerAttackEntityEvent
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageEvent

object InGameEvents : Listener {

    @EventHandler
    fun onPlayerDamage(e: EntityDamageEvent) {
        if (e.entityType != EntityType.PLAYER || e.cause == EntityDamageEvent.DamageCause.VOID) return
    }

    @EventHandler
    fun onPrePlayerAttack(e: PrePlayerAttackEntityEvent) {
        e.cancelEvent(*MinigameState.waitingForPlayersStates())
    }

    @EventHandler
    fun onPlayerBlockPlace(e: BlockPlaceEvent) = e.handleGameEvent(*MinigameState.inGameStates()) { _, game ->
        game.addPlayerPlacedBlock(e.block.location)
    }

    @EventHandler
    fun onPlayerBlockBreak(e: BlockBreakEvent) = e.handleGameEvent(*MinigameState.inGameStates()) { player, game ->
        e.isCancelled = with(TurfWarsLogic) {
            !game.canBreakBlock(player, e.block)
        }

        if (e.isCancelled) {
            locale.breakBlockNotAllowed(player)
        }
    }

}