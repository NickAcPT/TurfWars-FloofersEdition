package io.github.nickacpt.event.core.config.i18n

data class I18nConfiguration(
    val staticPlaceholders: Map<String, String>,
    val messages: Map<String, String>
)
