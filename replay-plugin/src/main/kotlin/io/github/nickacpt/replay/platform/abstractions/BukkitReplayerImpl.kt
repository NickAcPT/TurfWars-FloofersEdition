package io.github.nickacpt.replay.platform.abstractions

import io.github.nickacpt.behaviours.replay.ReplaySystem
import io.github.nickacpt.behaviours.replay.abstractions.ReplayViewer
import io.github.nickacpt.behaviours.replay.model.Replay
import io.github.nickacpt.behaviours.replay.playback.Replayer
import io.github.nickacpt.behaviours.replay.playback.session.ReplaySession
import io.github.nickacpt.replay.platform.BukkitReplayPlatform
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class BukkitReplayerImpl :
    Replayer<ItemStack, Player, World, Entity, BukkitReplayPlatform,
            ReplaySystem<ItemStack, Player, World, Entity, BukkitReplayPlatform>> {
    override fun prepareReplaySession(
        replay: Replay,
        replaySession: ReplaySession<ItemStack, Player, World, Entity, BukkitReplayPlatform, ReplaySystem<ItemStack, Player, World, Entity, BukkitReplayPlatform>>,
        replayViewers: List<ReplayViewer>
    ) {
    }

}