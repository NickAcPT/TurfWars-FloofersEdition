package io.github.nickacpt.event.core.tags

import net.kyori.adventure.text.Component

data class PlayerTag(val name: String, val value: Component, val adminOnly: Boolean)