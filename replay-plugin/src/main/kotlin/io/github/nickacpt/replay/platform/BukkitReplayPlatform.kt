package io.github.nickacpt.replay.platform

import io.github.nickacpt.behaviours.replay.ReplaySystem
import io.github.nickacpt.behaviours.replay.abstractions.*
import io.github.nickacpt.behaviours.replay.model.Replay
import io.github.nickacpt.behaviours.replay.model.standard.location.HasLook
import io.github.nickacpt.behaviours.replay.model.standard.location.RecordableLocation
import io.github.nickacpt.behaviours.replay.model.standard.location.RecordableLocationWithLook
import io.github.nickacpt.behaviours.replay.model.standard.location.RecordableLocationWithoutLook
import io.github.nickacpt.behaviours.replay.playback.Replayer
import io.github.nickacpt.replay.platform.abstractions.BukkitReplayViewer
import io.github.nickacpt.replay.platform.abstractions.BukkitReplayWorld
import io.github.nickacpt.replay.platform.abstractions.BukkitReplayerImpl
import io.github.nickacpt.replay.platform.abstractions.entity.BukkitReplayEntity
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

object BukkitReplayPlatform : ReplayPlatform<BukkitReplayViewer, BukkitReplayWorld, BukkitReplayEntity> {

    fun convertIntoReplayViewer(viewer: Player): BukkitReplayViewer {
        return BukkitReplayViewer(viewer.uniqueId)
    }

    fun convertIntoReplayEntity(entity: Entity): ReplayEntity {
        return BukkitReplayEntity(entity)
    }

    fun convertIntoReplayWorld(world: World): BukkitReplayWorld {
        return BukkitReplayWorld(world.uid)
    }

    override fun <Platform : ReplayPlatform<BukkitReplayViewer, BukkitReplayWorld, BukkitReplayEntity>,
            System : ReplaySystem<BukkitReplayViewer, BukkitReplayWorld, BukkitReplayEntity, Platform>> createReplayer(
        replaySystem: System,
        replay: Replay
    ): Replayer<BukkitReplayViewer, BukkitReplayWorld, BukkitReplayEntity, Platform, System> {
        @Suppress("UNCHECKED_CAST")
        return BukkitReplayerImpl() as Replayer<BukkitReplayViewer, BukkitReplayWorld, BukkitReplayEntity, Platform, System>
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

    fun convertIntoPlatformLocation(location: RecordableLocation, world: World) =
        Location(world, 0.0, 0.0, 0.0).also { applyRecordableLocation(location, it) }

    internal fun applyRecordableLocation(location: RecordableLocation, platform: Location) {
        platform.set(location.x, location.y, location.z)
        if (location is HasLook) {
            platform.yaw = location.yaw
            platform.pitch = location.pitch
        }
    }
}