package io.github.nickacpt.event.core.display.scoreboard

import io.github.nickacpt.event.core.players.TurfPlayer
import net.kyori.adventure.text.Component
import java.util.*

data class PlayerScoreboardDisplayData(val title: Component, val lines: List<Component>) {
    internal val trackedLines = Stack<TrackedScoreboardLine>()

    internal fun applyToScoreboard(player: TurfPlayer) {
        SidebarManager.notifySidebarDisplay(player, this)
    }
}