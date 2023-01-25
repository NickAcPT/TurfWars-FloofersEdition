package io.github.nickacpt.event.config.i18n

data class I18nConfiguration(
    val staticPlaceholders: MutableMap<String, String> = mutableMapOf(),
    val messages: Map<String, String> = emptyMap()
)