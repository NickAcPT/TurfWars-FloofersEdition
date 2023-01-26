package io.github.nickacpt.event.turfwars.config

data class TurfWarsConfig(
    val game: TurfWarsGameConfig = TurfWarsGameConfig(),
    val lobby: ConfigurationLocation
)