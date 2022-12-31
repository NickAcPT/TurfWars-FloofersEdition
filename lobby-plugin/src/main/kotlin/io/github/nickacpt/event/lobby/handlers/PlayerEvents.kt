package io.github.nickacpt.event.lobby.handlers

import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent
import io.github.nickacpt.event.core.utils.resetPlayer
import io.github.nickacpt.event.lobby.LobbyPlugin
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.spigotmc.event.player.PlayerSpawnLocationEvent

object PlayerEvents : Listener {

    @EventHandler(ignoreCancelled = false)
    fun onPlayerDamaged(e: EntityDamageEvent) {
        val entity = e.entity

        // Check if the entity is a player
        if (e.entityType != EntityType.PLAYER || entity !is Player) return

        // If the damage is caused by Lava, teleport the player to the spawn
        if (e.cause == EntityDamageEvent.DamageCause.LAVA) {
            entity.resetPlayer(teleportToSpawn = true, canFly = null)
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
        e.player.resetPlayer(teleportToSpawn = true, canFly = true)
    }

    @EventHandler
    fun onPlayerJoin(e: PlayerJoinEvent) {
        e.joinMessage(null)
        e.player.resetPlayer(teleportToSpawn = false, canFly = true)
    }

    @EventHandler
    fun onPlayerLeave(e: PlayerQuitEvent) {
        e.quitMessage(null)
    }
}