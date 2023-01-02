package io.github.nickacpt.event.lobby.config.lobby

data class LobbyConfiguration(
    val spawn: ConfigurationLocation,
    val parkour: ParkourConfiguration,
    val worldTime: Long
)