package io.github.nickacpt.replay

import io.github.nickacpt.behaviours.replay.ReplaySystem
import io.github.nickacpt.replay.platform.BukkitReplayPlatform
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

class ReplayPlugin : JavaPlugin() {
    companion object {
        val replaySystem = ReplaySystem<ItemStack, BukkitReplayPlatform>(BukkitReplayPlatform)
    }

}