package io.github.nickacpt.event.core.display.scoreboard

import net.kyori.adventure.text.Component

data class PlayerScoreboardDisplayData(val title: Component, val lines: List<Component>)