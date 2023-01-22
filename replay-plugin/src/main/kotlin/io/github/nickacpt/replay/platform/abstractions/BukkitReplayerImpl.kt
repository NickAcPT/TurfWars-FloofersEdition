package io.github.nickacpt.replay.platform.abstractions

import io.github.nickacpt.behaviours.replay.ReplaySystem
import io.github.nickacpt.behaviours.replay.model.Replay
import io.github.nickacpt.behaviours.replay.playback.Replayer
import io.github.nickacpt.behaviours.replay.playback.session.ReplaySession
import io.github.nickacpt.replay.platform.BukkitReplayPlatform
import io.github.nickacpt.replay.platform.abstractions.entity.BukkitRecordableReplayEntity
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.util.TriState
import org.bukkit.*
import java.util.*

class BukkitReplayerImpl :
    Replayer<BukkitReplayViewer, BukkitRecordableReplayEntity, BukkitReplayPlatform,
            ReplaySystem<BukkitReplayViewer, BukkitRecordableReplayEntity, BukkitReplayPlatform>> {

    companion object {
        const val REPLAY_WORLD_NAME_PRFIX = "replay_world_"
    }

    override fun prepareReplaySession(
        replay: Replay,
        replaySession: ReplaySession<BukkitReplayViewer, BukkitRecordableReplayEntity, BukkitReplayPlatform,
                ReplaySystem<BukkitReplayViewer, BukkitRecordableReplayEntity, BukkitReplayPlatform>>,
        replayViewers: List<BukkitReplayViewer>
    ) {
        val voidWorld = WorldCreator(REPLAY_WORLD_NAME_PRFIX + UUID.randomUUID().toString().replace("-", ""))
            .type(WorldType.FLAT)
            .generatorSettings("{\"layers\":[{\"block\":\"air\",\"height\":1}],\"biome\":\"plains\"}")
            .keepSpawnLoaded(TriState.FALSE)
            .createWorld()

        if (voidWorld == null) {
            replaySession.sendMessage(
                Component.text(
                    "Unable to create a world to host the replay session.",
                    NamedTextColor.RED
                )
            )
            return
        }

        // TODO: Load chunks
        voidWorld.setBlockData(0, 99, 0, Bukkit.createBlockData(Material.BEDROCK))

        replayViewers.forEach {
            setupReplayViewer(it, voidWorld)
        }
    }

    private fun setupReplayViewer(viewer: BukkitReplayViewer, world: World) {
        val player = viewer.bukkitPlayer ?: return

        player.teleportAsync(Location(world, 0.0, 100.0, 0.0)).thenAccept {
            player.gameMode = GameMode.ADVENTURE
            player.allowFlight = true
            player.isFlying = true
        }
    }

}