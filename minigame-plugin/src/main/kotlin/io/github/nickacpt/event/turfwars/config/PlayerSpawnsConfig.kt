package io.github.nickacpt.event.turfwars.config

data class PlayerSpawnsConfig(
    val spectator: ConfigurationLocation,
    val red: List<ConfigurationLocation> = emptyList(),
    val blue: List<ConfigurationLocation> = emptyList()
)