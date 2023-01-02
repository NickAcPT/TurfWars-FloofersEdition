package io.github.nickacpt.event.lobby.handlers

import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent
import io.github.nickacpt.event.lobby.LobbyPlugin
import io.github.nickacpt.event.utils.refreshPlayer
import org.bukkit.block.ShulkerBox
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.spigotmc.event.player.PlayerSpawnLocationEvent

object PlayerEvents : Listener {

    @EventHandler
    fun onPlayerInteractWithFoxBrain(e: InventoryClickEvent) {
        // Prevent players from interacting with the items inside the fox brain shulker box
        if (e.inventory.getHolder(false) is ShulkerBox) {
            e.isCancelled = true
        }
    }

    @EventHandler(ignoreCancelled = false)
    fun onPlayerDamaged(e: EntityDamageEvent) {
        val entity = e.entity

        // Check if the entity is a player
        if (e.entityType != EntityType.PLAYER || entity !is Player) return

        // If the damage is caused by Lava, teleport the player to the spawn
        if (e.cause == EntityDamageEvent.DamageCause.LAVA) {
            LobbyPlugin.messages.fellInLava(entity)
            entity.refreshPlayer(teleportToSpawn = true, canFly = null)
        }

        // Cancel all damage to players
        e.isCancelled = true
    }

    @EventHandler
    fun onPlayerSpawn(e: PlayerSpawnLocationEvent) {
        e.spawnLocation = LobbyPlugin.instance.config.spawn.toBukkitLocation()
    }

    @EventHandler
    fun onPlayerPostSpawn(e: PlayerPostRespawnEvent) {
        e.player.refreshPlayer(teleportToSpawn = true, canFly = true)
    }
}