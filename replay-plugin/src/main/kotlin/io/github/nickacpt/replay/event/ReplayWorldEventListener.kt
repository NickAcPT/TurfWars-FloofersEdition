package io.github.nickacpt.replay.event

import io.github.nickacpt.replay.platform.abstractions.BukkitReplayerImpl
import org.bukkit.GameRule
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.world.ChunkUnloadEvent
import org.bukkit.event.world.WorldLoadEvent
import org.bukkit.event.world.WorldUnloadEvent

object ReplayWorldEventListener : Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    fun handleReplayWorldUnload(event: WorldUnloadEvent) {
        if (event.world.name.startsWith(BukkitReplayerImpl.REPLAY_WORLD_NAME_PRFIX)) {
            event.world.worldFolder.deleteRecursively()
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun handleReplayWorldLoad(event: WorldLoadEvent) {
        val world = event.world
        if (world.name.startsWith(BukkitReplayerImpl.REPLAY_WORLD_NAME_PRFIX)) {
            world.isAutoSave = false
            world.keepSpawnInMemory = false

            world.time = 6000
            world.isThundering = false
            world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false)
            world.setGameRule(GameRule.DO_WEATHER_CYCLE, false)
            world.setGameRule(GameRule.DO_MOB_SPAWNING, false)

            world.setGameRule(GameRule.RANDOM_TICK_SPEED, 0)
            world.setGameRule(GameRule.DO_INSOMNIA, false)
            world.setGameRule(GameRule.DO_FIRE_TICK, false)

        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun handleReplayWorldChunkUnload(event: ChunkUnloadEvent) {
        if (event.world.name.startsWith(BukkitReplayerImpl.REPLAY_WORLD_NAME_PRFIX)) {
            event.isSaveChunk = false
        }
    }

}
