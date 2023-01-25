package io.github.nickacpt.event.turfwars.config

data class TurfWarsGameConfig(
    val minimumPlayers: Int = 2,
    val maximumPlayers: Int = 16,
    val lobbyCountdown: Int = 15,
)