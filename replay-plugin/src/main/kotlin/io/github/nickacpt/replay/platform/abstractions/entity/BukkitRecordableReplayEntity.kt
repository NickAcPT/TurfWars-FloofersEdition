package io.github.nickacpt.replay.platform.abstractions.entity

import io.github.nickacpt.behaviours.replay.abstractions.RecordableReplayEntity
import io.github.nickacpt.behaviours.replay.abstractions.ReplayEntityData
import io.github.nickacpt.behaviours.replay.model.standard.location.RecordableLocation
import io.github.nickacpt.replay.platform.BukkitReplayPlatform
import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

class BukkitRecordableReplayEntity(entity: Entity) : RecordableReplayEntity {
    private val entityUuid = entity.uniqueId
    private val bukkitEntity = Bukkit.getEntity(entityUuid)

    override val id: Int = entity.entityId

    override val location: RecordableLocation
        get() = BukkitReplayPlatform.convertIntoReplayLocation(bukkitEntity?.location)

    override val data: ReplayEntityData
        get() = when (bukkitEntity) {
            is Player -> PlayerEntityData(PlayerGameProfile.fromBukkitProfile(bukkitEntity))
            null -> EmptyEntityData
            else -> SerializedEntityData(Bukkit.getUnsafe().serializeEntity(bukkitEntity))
        }
}