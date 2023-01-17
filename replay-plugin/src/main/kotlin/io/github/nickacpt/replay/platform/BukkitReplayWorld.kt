package io.github.nickacpt.replay.platform

import io.github.nickacpt.behaviours.replay.abstractions.ReplayWorld
import java.util.*

data class BukkitReplayWorld(override val id: UUID): ReplayWorld
