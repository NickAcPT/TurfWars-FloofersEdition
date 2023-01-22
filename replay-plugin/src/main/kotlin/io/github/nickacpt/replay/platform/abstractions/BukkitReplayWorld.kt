package io.github.nickacpt.replay.platform.abstractions

import io.github.nickacpt.behaviours.replay.abstractions.RecordableReplayEntity
import io.github.nickacpt.behaviours.replay.abstractions.ReplayWorld
import io.github.nickacpt.replay.platform.BukkitReplayPlatform
import org.bukkit.Bukkit
import java.util.*

data class BukkitReplayWorld(override val id: UUID) : ReplayWorld {

    private val bukkitWorld get() = Bukkit.getWorld(id)

    override val entities: List<RecordableReplayEntity>
        get() = bukkitWorld?.entities
            ?.map { BukkitReplayPlatform.convertIntoReplayEntity(it) } ?: emptyList()
}
