package io.github.nickacpt.event.core.display.scoreboard

import org.bukkit.scoreboard.Score
import org.bukkit.scoreboard.Team

data class TrackedScoreboardLine(val index: Int, val score: Score, val team: Team) {
    fun stopTracking() {
        score.resetScore()
    }
}