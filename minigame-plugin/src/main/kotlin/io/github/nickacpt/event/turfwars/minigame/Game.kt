package io.github.nickacpt.event.turfwars.minigame

import io.github.nickacpt.event.core.players.TurfPlayer
import java.util.*

data class Game(
    val id: UUID,
    val players: List<TurfPlayer>,
) {
    val state: MinigameState = MinigameState.WAITING
}