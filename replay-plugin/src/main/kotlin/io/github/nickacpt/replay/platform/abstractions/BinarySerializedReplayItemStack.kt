package io.github.nickacpt.replay.platform.abstractions

import io.github.nickacpt.behaviours.replay.abstractions.ReplayItemStack

data class BinarySerializedReplayItemStack(val bytes: ByteArray) : ReplayItemStack {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BinarySerializedReplayItemStack

        if (!bytes.contentEquals(other.bytes)) return false

        return true
    }

    override fun hashCode(): Int {
        return bytes.contentHashCode()
    }
}