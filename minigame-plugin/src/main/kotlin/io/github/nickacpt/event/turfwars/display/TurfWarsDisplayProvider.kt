package io.github.nickacpt.event.turfwars.display

import io.github.nickacpt.event.core.display.PlayerDisplayProvider
import io.github.nickacpt.event.core.display.scoreboard.PlayerScoreboardDisplayData
import io.github.nickacpt.event.core.players.TurfPlayer
import io.github.nickacpt.event.turfwars.minigame.TurfWarsGame.Companion.game
import io.github.nickacpt.event.turfwars.minigame.logic.TurfWarsLogic

object TurfWarsDisplayProvider : PlayerDisplayProvider {

    override fun provideScoreboardDisplay(player: TurfPlayer): PlayerScoreboardDisplayData? {
        val game = player.game ?: return null

        return with(TurfWarsLogic) { game.getScoreboardLines(player) }?.let {
            PlayerScoreboardDisplayData(TurfWarsLogic.gameScoreboardTitle, it)
        }
    }
}