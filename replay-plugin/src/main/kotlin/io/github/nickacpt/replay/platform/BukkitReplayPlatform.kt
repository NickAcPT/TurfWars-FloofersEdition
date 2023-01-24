package io.github.nickacpt.replay.platform

import io.github.nickacpt.behaviours.replay.ReplaySystem
import io.github.nickacpt.behaviours.replay.abstractions.*
import io.github.nickacpt.behaviours.replay.model.Replay
import io.github.nickacpt.behaviours.replay.model.standard.location.HasLook
import io.github.nickacpt.behaviours.replay.model.standard.location.RecordableLocation
import io.github.nickacpt.behaviours.replay.model.standard.location.RecordableLocationWithLook
import io.github.nickacpt.behaviours.replay.model.standard.location.RecordableLocationWithoutLook
import io.github.nickacpt.behaviours.replay.playback.Replayer
import io.github.nickacpt.replay.ReplayPlugin
import io.github.nickacpt.replay.platform.abstractions.BukkitReplayViewer
import io.github.nickacpt.replay.platform.abstractions.BukkitReplayWorld
import io.github.nickacpt.replay.platform.abstractions.entity.BukkitReplayEntity
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import java.util.*

object BukkitReplayPlatform : ReplayPlatform<BukkitReplayWorld, BukkitReplayViewer, BukkitReplayEntity> {

    private val viewerCache = mutableMapOf<UUID, BukkitReplayViewer>()
    private val worldCache = mutableMapOf<UUID, BukkitReplayWorld>()

    fun convertIntoReplayViewer(viewer: Player): BukkitReplayViewer {
        return viewerCache.getOrPut(viewer.uniqueId) { BukkitReplayViewer(viewer.uniqueId) }
    }

    fun convertIntoReplayEntity(entity: Entity): BukkitReplayEntity {
        return BukkitReplayEntity(entity)
    }

    fun convertIntoReplayWorld(world: World): BukkitReplayWorld {
        return worldCache.getOrPut(world.uid) { BukkitReplayWorld(world.uid) }
    }

    override fun <Platform : ReplayPlatform<BukkitReplayWorld, BukkitReplayViewer, BukkitReplayEntity>,
            System : ReplaySystem<BukkitReplayWorld, BukkitReplayViewer, BukkitReplayEntity, Platform>> createReplayer(
        replaySystem: System,
        replay: Replay
    ): Replayer<BukkitReplayWorld, BukkitReplayViewer, BukkitReplayEntity, Platform, System> {
        @Suppress("UNCHECKED_CAST")
        return BukkitReplayerImpl() as Replayer<BukkitReplayWorld, BukkitReplayViewer, BukkitReplayEntity, Platform, System>
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

    override fun registerRepeatingTask(delay: Long, task: () -> Unit) {
        Bukkit.getScheduler().runTaskTimer(ReplayPlugin.instance, task, 0, delay)
    }
}