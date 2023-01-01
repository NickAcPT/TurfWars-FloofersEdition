package io.github.nickacpt.event.core.config

data class CoreConfig(
    val tags: Map<String, String> = emptyMap(),
    val adminTags: Map<String, String> = emptyMap(),
)
