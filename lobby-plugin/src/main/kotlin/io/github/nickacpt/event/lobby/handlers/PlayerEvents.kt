package io.github.nickacpt.event.lobby.handlers

import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerInteractEvent

object PlayerEvents : Listener {

    @EventHandler
    fun onPlayerDamaged(e: EntityDamageEvent) {
        // Check if the entity is a player
        if (e.entityType != EntityType.PLAYER) return

        // Cancel all damage to players
        e.isCancelled = true

        // If the damage is caused by Lava, teleport the player to the spawn
        if (e.cause == EntityDamageEvent.DamageCause.LAVA) {
            e.entity.teleport(e.entity.world.spawnLocation)
        }
    }
}