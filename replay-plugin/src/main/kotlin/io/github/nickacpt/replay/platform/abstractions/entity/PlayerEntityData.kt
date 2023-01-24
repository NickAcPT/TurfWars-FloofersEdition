package io.github.nickacpt.replay.platform.abstractions.entity

import io.github.nickacpt.behaviours.replay.abstractions.ReplayEntityData

data class PlayerEntityData(
    val profile: PlayerGameProfile,
    val skinParts: Int
) : ReplayEntityData