package io.github.nickacpt.event.core.config

data class CoreConfig(
    val forgetPlayersOnQuit: Boolean = true,
    val scoreboard: ScoreboardConfig = ScoreboardConfig(emptyList()),
    val database: DatabaseConfig = DatabaseConfig(),
)
