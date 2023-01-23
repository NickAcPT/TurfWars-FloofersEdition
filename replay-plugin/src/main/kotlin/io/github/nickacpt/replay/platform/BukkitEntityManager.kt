package io.github.nickacpt.replay.platform

import io.github.nickacpt.behaviours.replay.ReplaySystem
import io.github.nickacpt.behaviours.replay.abstractions.EntityManager
import io.github.nickacpt.behaviours.replay.abstractions.ReplayPlatform
import io.github.nickacpt.behaviours.replay.model.RecordedReplayEntity
import io.github.nickacpt.behaviours.replay.model.standard.location.HasLook
import io.github.nickacpt.behaviours.replay.model.standard.location.RecordableLocation
import io.github.nickacpt.behaviours.replay.playback.session.ReplaySession
import io.github.nickacpt.replay.platform.abstractions.BukkitReplayViewer
import io.github.nickacpt.replay.platform.abstractions.BukkitReplayWorld
import io.github.nickacpt.replay.platform.abstractions.entity.BukkitReplayEntity

class BukkitEntityManager<
        Platform : ReplayPlatform<BukkitReplayViewer, BukkitReplayWorld, BukkitReplayEntity>,
        System : ReplaySystem<BukkitReplayViewer, BukkitReplayWorld, BukkitReplayEntity, Platform>,
        Session : ReplaySession<BukkitReplayViewer, BukkitReplayWorld, BukkitReplayEntity, Platform, System>,
        >(private val session: Session) : EntityManager<BukkitReplayEntity> {
    override fun spawnEntity(entity: RecordedReplayEntity, location: RecordableLocation): BukkitReplayEntity {
        val world = session.world.bukkitWorld ?: throw IllegalStateException("World is not loaded")

        TODO("Not yet implemented")
    }

    override fun removeEntity(entity: BukkitReplayEntity) {
        entity.bukkitEntity?.remove()
    }

    override fun updateEntityPosition(entity: BukkitReplayEntity, location: RecordableLocation) {
        val bukkitEntity = entity.bukkitEntity ?: return

        bukkitEntity.location.set(location.x, location.y, location.z)
        if (location is HasLook) {
            bukkitEntity.location.yaw = location.yaw
            bukkitEntity.location.pitch = location.pitch
        }
    }
}
