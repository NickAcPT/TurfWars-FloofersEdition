package io.github.nickacpt.event.core.display.scoreboard

import net.kyori.adventure.text.Component
import java.util.*

data class TrackingPlayerScoreboardDisplayData(var title: Component?, var lines: List<Component>?) {
    internal val trackedLines = Stack<TrackedScoreboardLine>()
}