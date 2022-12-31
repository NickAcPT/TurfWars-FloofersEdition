package io.github.nickacpt.event.core.handlers

import org.bukkit.GameRule
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.world.WorldLoadEvent

object WorldEvents : Listener {

    @EventHandler
    fun onWorldLoad(e: WorldLoadEvent) {
        val world = e.world

        world.setGameRule(GameRule.DISABLE_RAIDS, true)

        world.setGameRule(GameRule.DO_FIRE_TICK, false)
        world.setGameRule(GameRule.DO_MOB_SPAWNING, false)
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, false)
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false)

        world.setGameRule(GameRule.RANDOM_TICK_SPEED, 0)
    }

}
