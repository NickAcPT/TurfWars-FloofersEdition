package io.github.nickacpt.replay.platform

import io.github.nickacpt.behaviours.replay.ReplaySystem
import io.github.nickacpt.behaviours.replay.abstractions.*
import io.github.nickacpt.behaviours.replay.model.Replay
import io.github.nickacpt.behaviours.replay.model.standard.location.RecordableLocation
import io.github.nickacpt.behaviours.replay.model.standard.location.RecordableLocationWithLook
import io.github.nickacpt.behaviours.replay.model.standard.location.RecordableLocationWithoutLook
import io.github.nickacpt.behaviours.replay.playback.Replayer
import io.github.nickacpt.replay.platform.abstractions.BukkitReplayViewer
import io.github.nickacpt.replay.platform.abstractions.BukkitReplayWorld
import io.github.nickacpt.replay.platform.abstractions.BukkitReplayerImpl
import io.github.nickacpt.replay.platform.abstractions.entity.BukkitRecordableReplayEntity
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

object BukkitReplayPlatform : ReplayPlatform<BukkitReplayViewer, BukkitRecordableReplayEntity> {

    fun convertIntoReplayViewer(viewer: Player): BukkitReplayViewer {
        return BukkitReplayViewer(viewer.uniqueId)
    }

    fun convertIntoReplayEntity(entity: Entity): RecordableReplayEntity {
        return BukkitRecordableReplayEntity(entity)
    }

    fun convertIntoReplayWorld(world: World): ReplayWorld {
        return BukkitReplayWorld(world.uid)
    }

    override fun <Platform : ReplayPlatform<BukkitReplayViewer, BukkitRecordableReplayEntity>,
            System : ReplaySystem<BukkitReplayViewer, BukkitRecordableReplayEntity, Platform>> createReplayer(
        replaySystem: System,
        replay: Replay
    ): Replayer<BukkitReplayViewer, BukkitRecordableReplayEntity, Platform, System> {
        @Suppress("UNCHECKED_CAST")
        return BukkitReplayerImpl() as Replayer<BukkitReplayViewer, BukkitRecordableReplayEntity, Platform, System>
    }

    fun convertIntoReplayLocation(location: Location?): RecordableLocation {
        if (location == null) return RecordableLocationWithoutLook(0.0, 0.0, 0.0)

        return RecordableLocationWithLook(
            location.x,
            location.y,
            location.z,
            location.yaw,
            location.pitch
        )
    }
}