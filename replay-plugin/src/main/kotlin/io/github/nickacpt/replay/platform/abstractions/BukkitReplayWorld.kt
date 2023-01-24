package io.github.nickacpt.replay.platform.abstractions

import io.github.nickacpt.behaviours.replay.ReplaySystem
import io.github.nickacpt.behaviours.replay.abstractions.ReplayEntity
import io.github.nickacpt.behaviours.replay.abstractions.ReplayWorld
import io.github.nickacpt.behaviours.replay.playback.session.ReplaySession
import io.github.nickacpt.replay.platform.BukkitReplayPlatform
import io.github.nickacpt.replay.platform.abstractions.entity.BukkitReplayEntity
import org.bukkit.Bukkit
import java.util.*

data class BukkitReplayWorld(override val id: UUID) : ReplayWorld {

    internal val bukkitWorld get() = Bukkit.getWorld(id)

    override val entities: List<ReplayEntity>
        get() = bukkitWorld?.entities
            ?.map { BukkitReplayPlatform.convertIntoReplayEntity(it) } ?: emptyList()

    var session: ReplaySession<
            BukkitReplayWorld,
            BukkitReplayViewer,
            BukkitReplayEntity,
            BukkitReplayPlatform,
            ReplaySystem<BukkitReplayWorld, BukkitReplayViewer, BukkitReplayEntity, BukkitReplayPlatform>
            >? = null
}
