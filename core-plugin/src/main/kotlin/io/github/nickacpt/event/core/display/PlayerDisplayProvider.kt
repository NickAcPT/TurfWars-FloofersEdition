package io.github.nickacpt.event.core.display

import io.github.nickacpt.event.core.display.scoreboard.PlayerScoreboardDisplayData
import io.github.nickacpt.event.core.players.TurfPlayer

interface PlayerDisplayProvider {
    fun provideDisplay(player: TurfPlayer): PlayerDisplayData? {
        return null
    }
    fun provideScoreboardDisplay(player: TurfPlayer): PlayerScoreboardDisplayData? {
        return null
    }
}