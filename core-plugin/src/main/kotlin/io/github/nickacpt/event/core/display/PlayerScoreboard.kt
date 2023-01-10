package io.github.nickacpt.event.core.display

import net.kyori.adventure.text.Component

data class PlayerScoreboard(val title: Component, val lines: List<PlayerScoreboardLine>)