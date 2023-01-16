package io.github.nickacpt.replay.platform

import io.github.nickacpt.behaviours.replay.abstractions.ReplayViewer
import net.kyori.adventure.audience.Audience
import org.bukkit.Bukkit
import java.util.*

data class BukkitReplayViewer(override val id: UUID) : ReplayViewer {
    override val audience: Audience
        get() = Bukkit.getPlayer(id) ?: Audience.empty()
}
