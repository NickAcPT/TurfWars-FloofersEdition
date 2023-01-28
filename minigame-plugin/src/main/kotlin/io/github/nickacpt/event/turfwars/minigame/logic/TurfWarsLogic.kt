package io.github.nickacpt.event.turfwars.minigame.logic

import io.github.nickacpt.event.core.players.TurfPlayer
import io.github.nickacpt.event.turfwars.TurfWarsPlugin.Companion.config
import io.github.nickacpt.event.turfwars.TurfWarsPlugin.Companion.locale
import io.github.nickacpt.event.turfwars.minigame.MinigameState
import io.github.nickacpt.event.turfwars.minigame.TurfWarsGame
import io.github.nickacpt.event.turfwars.minigame.logic.states.game.BaseTurfStateLogic
import io.github.nickacpt.event.turfwars.minigame.teams.TurfWarsTeam
import io.github.nickacpt.event.turfwars.minigame.teams.TurfWarsTeam.Companion.team
import io.github.nickacpt.event.utils.joinTo
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.space
import net.kyori.adventure.text.JoinConfiguration
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.block.Block
import kotlin.math.max

object TurfWarsLogic {

    fun TurfWarsGame.canBreakBlock(player: TurfPlayer, block: Block): Boolean {
        return player.team != this.spectatorTeam && this.isBlockPlacedByPlayer(block.location)
    }


    fun TurfWarsGame.onAddPlayerToTeam(player: TurfPlayer, team: TurfWarsTeam) {

    }

    fun TurfWarsGame.onRemovePlayerFromTeam(player: TurfPlayer, team: TurfWarsTeam) {

    }

    private fun TurfWarsGame.moveToNextState(previousState: MinigameState): MinigameState? {
        val logics = previousState.stateLogics

        for (logic in logics) {
            val nextState = with(logic) { tickState() }
            if (nextState != null) {
                return nextState
            }
        }

        return null
    }

    fun TurfWarsGame.tickGame() {
        val currentState = state
        val nextState = moveToNextState(currentState)
        if (nextState != null && nextState != currentState) {
            state = nextState
            players.forEach { it.refresh() }
        }
    }

    val gameScoreboardTitle by lazy { locale.scoreboardTitle() }

    fun TurfWarsGame.getScoreboardLines(player: TurfPlayer): List<Component> {
        // If the game is waiting for players, we should show the state and the amount of players
        val list = mutableListOf<List<Component>>()

        if (state.showsStateInScoreboard()) {
            list += listOf(
                Component.text("Players", null, TextDecoration.BOLD),
                locale.scoreboardPlayerCount(playerCount, max(config.game.maximumPlayers, playerCount))
            )

            list += listOf(
                Component.text("State", null, TextDecoration.BOLD),
                state.descriptionAsComponent(this)
            )
        }

        if (state.isInGame()) {
            val description = state.descriptionAsComponent(this)
            val turfStateLogic = state.stateLogics.firstNotNullOfOrNull { it as? BaseTurfStateLogic }

            if (turfStateLogic != null) {
                list += listOf(
                    Component.join(
                        JoinConfiguration.separator(space()),
                        description,
                        Component.text("Time")
                    ).mergeStyle(description).decorate(TextDecoration.BOLD),
                    turfStateLogic.timer(this).remainingTime()
                )
            }

        }

        return list.joinTo(mutableListOf(), listOf(space())).flatten()
    }

}