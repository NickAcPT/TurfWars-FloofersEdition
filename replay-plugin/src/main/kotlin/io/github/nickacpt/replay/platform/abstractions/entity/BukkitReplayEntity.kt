package io.github.nickacpt.replay.platform.abstractions.entity

import io.github.nickacpt.behaviours.replay.abstractions.ReplayEntity
import io.github.nickacpt.behaviours.replay.abstractions.ReplayEntityData
import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

class BukkitReplayEntity(entity: Entity) : ReplayEntity {
    private val entityUuid = entity.uniqueId
    private val bukkitEntity = Bukkit.getEntity(entityUuid)

    override val id: Int = entity.entityId

    override val data: ReplayEntityData
        get() = when (bukkitEntity) {
            is Player -> PlayerEntityData(PlayerGameProfile.fromBukkitProfile(bukkitEntity))
            null -> EmptyEntityData
            else -> SerializedEntityData(Bukkit.getUnsafe().serializeEntity(bukkitEntity))
        }
}