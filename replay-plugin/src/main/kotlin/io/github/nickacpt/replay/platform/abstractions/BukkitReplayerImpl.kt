package io.github.nickacpt.replay.platform.abstractions

import io.github.nickacpt.behaviours.replay.ReplaySystem
import io.github.nickacpt.behaviours.replay.playback.Replayer
import io.github.nickacpt.replay.platform.BukkitReplayPlatform
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class BukkitReplayerImpl :
    Replayer<ItemStack, Player, World, Entity, BukkitReplayPlatform,
            ReplaySystem<ItemStack, Player, World, Entity, BukkitReplayPlatform>> {

}