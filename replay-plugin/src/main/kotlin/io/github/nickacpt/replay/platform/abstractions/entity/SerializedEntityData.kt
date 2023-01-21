package io.github.nickacpt.replay.platform.abstractions.entity

import io.github.nickacpt.behaviours.replay.abstractions.ReplayEntityData

data class SerializedEntityData(
    val serializedData: ByteArray
) : ReplayEntityData {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SerializedEntityData

        if (!serializedData.contentEquals(other.serializedData)) return false

        return true
    }

    override fun hashCode(): Int {
        return serializedData.contentHashCode()
    }
}