package io.github.nickacpt.event.core.config

data class CoreConfig(
    val forgetPlayersOnQuit: Boolean = true,
    val tags: Map<String, String> = emptyMap(),
    val adminTags: Map<String, String> = emptyMap(),
    val scoreboard: ScoreboardConfig = ScoreboardConfig(emptyList()),
)
