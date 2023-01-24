package io.github.nickacpt.replay.platform.abstractions

import io.github.nickacpt.behaviours.replay.abstractions.ReplayViewer
import io.github.nickacpt.replay.platform.BukkitReplayPlatform
import net.kyori.adventure.audience.Audience
import org.bukkit.Bukkit
import java.util.*

data class BukkitReplayViewer(override val id: UUID) : ReplayViewer<BukkitReplayWorld> {
    internal val bukkitPlayer get() = Bukkit.getPlayer(id)

    override val audience: Audience
        get() = bukkitPlayer ?: Audience.empty()

    override val world: BukkitReplayWorld?
        get() = bukkitPlayer?.world?.let { BukkitReplayPlatform.convertIntoReplayWorld(it) }
}
