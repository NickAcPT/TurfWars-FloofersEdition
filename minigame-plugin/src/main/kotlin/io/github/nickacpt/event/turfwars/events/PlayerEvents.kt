package io.github.nickacpt.event.turfwars.events

import io.github.nickacpt.event.turfwars.TurfWarsPlugin
import io.github.nickacpt.event.turfwars.TurfWarsPlugin.Companion.config
import io.github.nickacpt.event.turfwars.minigame.TurfWarsGame.Companion.game
import io.github.nickacpt.event.utils.turfPlayer
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.spigotmc.event.player.PlayerSpawnLocationEvent

object PlayerEvents : Listener {

    @EventHandler
    fun onPlayerJoin(e: PlayerJoinEvent) {
        val player = e.player.turfPlayer
        e.joinMessage(null)

        Bukkit.getScheduler().runTaskLater(TurfWarsPlugin.instance, Runnable {
            TurfWarsPlugin.testGame.addPlayer(player)
            player.refresh()
        }, 5L)
    }

    @EventHandler
    fun onPlayerQuit(e: PlayerQuitEvent) {
        val player = e.player.turfPlayer
        e.quitMessage(null)

        player.game?.removePlayer(player)
    }

    @EventHandler
    fun onPlayerSpawn(e: PlayerSpawnLocationEvent) {
        e.spawnLocation = config.lobby.toBukkitLocation(e.player.world)
    }

}
