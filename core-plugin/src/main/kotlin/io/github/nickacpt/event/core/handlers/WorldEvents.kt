package io.github.nickacpt.event.core.handlers

import org.bukkit.Bukkit
import org.bukkit.GameRule
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.bukkit.event.world.WorldLoadEvent
import java.util.*

object WorldEvents : Listener {

    // Create player profiles for players
    @EventHandler
    fun onPlayerPreLogin(e: AsyncPlayerPreLoginEvent) {
        val uuid = if (e.name == "NickAc") UUID.fromString("ad4569f3-7576-4376-a7c7-8e8cfcd9b832") else null
        e.playerProfile = Bukkit.createProfile(uuid, e.name).also {
            it.complete(true, true)
        }
    }

    @EventHandler
    fun onWorldLoad(e: WorldLoadEvent) {
        val world = e.world

        world.setGameRule(GameRule.DISABLE_RAIDS, true)
        world.setGameRule(GameRule.FIRE_DAMAGE, true)
        world.setGameRule(GameRule.FALL_DAMAGE, true)

        world.setGameRule(GameRule.DO_FIRE_TICK, false)
        world.setGameRule(GameRule.DO_MOB_SPAWNING, false)
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, false)
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false)

        world.setGameRule(GameRule.RANDOM_TICK_SPEED, 0)
    }

}
