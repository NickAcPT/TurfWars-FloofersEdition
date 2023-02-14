package io.github.nickacpt.event.turfwars.config

data class TurfWarsGameConfig(
    val minimumPlayers: Int = 2,
    val maximumPlayers: Int = 16,

    val lobbyCountdown: Int = 15,

    val buildTime: Int = 40,
    val combatTime: Int = 60,
    val endTime: Int = 900 /* 15 minutes = 15 * 60 seconds */,

    val playerSpawns: PlayerSpawnsConfig
)

data class PlayerSpawnsConfig(
    val spectator: ConfigurationLocation,
    val red: List<ConfigurationLocation> = emptyList(),
    val blue: List<ConfigurationLocation> = emptyList()
)

