package io.github.nickacpt.event.turfwars.display

import io.github.nickacpt.event.core.display.PlayerDisplayData
import io.github.nickacpt.event.core.display.PlayerDisplayProvider
import io.github.nickacpt.event.core.display.scoreboard.PlayerScoreboardDisplayData
import io.github.nickacpt.event.core.players.TurfPlayer
import io.github.nickacpt.event.turfwars.minigame.TurfWarsGame.Companion.game
import io.github.nickacpt.event.turfwars.minigame.logic.TurfWarsLogic
import io.github.nickacpt.event.turfwars.minigame.teams.TurfWarsTeam.Companion.team
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration

object TurfWarsDisplayProvider : PlayerDisplayProvider {

    override fun provideDisplay(player: TurfPlayer): PlayerDisplayData? {
        return player.team?.let {
            val teamName = Component.text(it.name.uppercase(), null, TextDecoration.BOLD)
            return PlayerDisplayData(teamName.color(it.color))
        }
    }

    override fun provideScoreboardDisplay(player: TurfPlayer): PlayerScoreboardDisplayData? {
        val game = player.game ?: return null

        return with(TurfWarsLogic) { game.getScoreboardLines(player) }.let {
            PlayerScoreboardDisplayData(TurfWarsLogic.gameScoreboardTitle, it)
        }
    }
}