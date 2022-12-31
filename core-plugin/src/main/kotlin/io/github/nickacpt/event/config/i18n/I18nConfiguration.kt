package io.github.nickacpt.event.config.i18n

data class I18nConfiguration(
    val staticPlaceholders: Map<String, String> = emptyMap(),
    val messages: Map<String, String> = emptyMap()
)