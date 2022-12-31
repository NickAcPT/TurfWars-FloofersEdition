package io.github.nickacpt.event.lobby.handlers

import io.github.nickacpt.event.lobby.LobbyPlugin
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.world.WorldLoadEvent
import org.spigotmc.event.player.PlayerSpawnLocationEvent

object WorldEvents : Listener {
    @EventHandler
    fun onPlayerBreakBlock(e: BlockBreakEvent) {
        e.isCancelled = true
    }

    @EventHandler
    fun onPlayerPlaceBlock(e: BlockPlaceEvent) {
        e.isCancelled = true
    }


    @EventHandler
    fun onWorldLoad(e: WorldLoadEvent) {
        val config = LobbyPlugin.instance.config
        val world = e.world

        world.spawnLocation = config.spawn.toBukkitLocation()
        world.time = config.worldTime
    }
}