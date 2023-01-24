package io.github.nickacpt.replay.platform

import io.github.nickacpt.behaviours.replay.ReplaySystem
import io.github.nickacpt.behaviours.replay.abstractions.EntityManager
import io.github.nickacpt.behaviours.replay.abstractions.ReplayPlatform
import io.github.nickacpt.behaviours.replay.model.RecordedReplayEntity
import io.github.nickacpt.behaviours.replay.model.standard.location.RecordableLocation
import io.github.nickacpt.behaviours.replay.playback.session.ReplaySession
import io.github.nickacpt.replay.platform.abstractions.BukkitReplayViewer
import io.github.nickacpt.replay.platform.abstractions.BukkitReplayWorld
import io.github.nickacpt.replay.platform.abstractions.entity.BukkitReplayEntity
import io.github.nickacpt.replay.platform.abstractions.entity.PlayerEntityData
import io.github.nickacpt.replay.platform.utils.NmsUtils
import java.util.concurrent.ConcurrentHashMap

class BukkitEntityManager<
        Platform : ReplayPlatform<BukkitReplayWorld, BukkitReplayViewer, BukkitReplayEntity>,
        System : ReplaySystem<BukkitReplayWorld, BukkitReplayViewer, BukkitReplayEntity, Platform>,
        Session : ReplaySession<BukkitReplayWorld, BukkitReplayViewer, BukkitReplayEntity, Platform, System>,
        >(private val session: Session) : EntityManager<BukkitReplayEntity> {

    override val entityMap: MutableMap<Int, BukkitReplayEntity> = ConcurrentHashMap()

    override fun spawnEntity(entity: RecordedReplayEntity, location: RecordableLocation): BukkitReplayEntity {
        val world = session.world.bukkitWorld ?: throw IllegalStateException("World is not loaded")
        val data = entity.data

        if (data is PlayerEntityData) {
            val npc = NmsUtils.createFakePlayer(
                world, data, location
            )

            world.players.forEach { player ->
                npc.spawn(player)
            }

            return npc
        } else {
            TODO("Not yet implemented")
        }
    }

    override fun removeEntity(entity: BukkitReplayEntity) {
        if (entity is BukkitReplayEntity.BukkitEntity) {
            entity.bukkitEntity?.remove()
        }
    }

    override fun updateEntityPosition(entity: BukkitReplayEntity, location: RecordableLocation) {
        if (entity is BukkitReplayEntity.FakePlayer) {
            entity.teleport(location)
        } else if (entity is BukkitReplayEntity.BukkitEntity) {
            val bukkitEntity = entity.bukkitEntity ?: return

            bukkitEntity.teleport(bukkitEntity.location.apply {
                BukkitReplayPlatform.applyRecordableLocation(
                    location,
                    this
                )
            })
        }
    }
}
