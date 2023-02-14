package io.github.nickacpt.event.turfwars.minigame.logic

import io.github.nickacpt.event.core.players.TurfPlayer
import io.github.nickacpt.event.turfwars.TurfWarsPlugin.Companion.config
import io.github.nickacpt.event.turfwars.TurfWarsPlugin.Companion.locale
import io.github.nickacpt.event.turfwars.minigame.MinigameState
import io.github.nickacpt.event.turfwars.minigame.TurfWarsGame
import io.github.nickacpt.event.turfwars.minigame.logic.ScoreboardElement.GroupScoreboardElement
import io.github.nickacpt.event.turfwars.minigame.logic.states.game.BaseTurfStateLogic
import io.github.nickacpt.event.turfwars.minigame.teams.TurfWarsTeam
import io.github.nickacpt.event.turfwars.minigame.teams.TurfWarsTeam.Companion.team
import io.github.nickacpt.event.utils.joinTo
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.space
import net.kyori.adventure.text.JoinConfiguration
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.block.Block
import kotlin.math.max


object TurfWarsLogic {

    fun TurfWarsGame.canBreakBlock(player: TurfPlayer, block: Block): Boolean {
        return player.team != this.spectatorTeam && player.team?.isBlockPlacedByPlayer(block.location) == true
    }

    fun TurfWarsGame.canPlaceBlock(player: TurfPlayer, block: Block): Boolean {
        return player.team != this.spectatorTeam
    }


    fun TurfWarsGame.onAddPlayerToTeam(player: TurfPlayer, team: TurfWarsTeam) {
        // Do whatever the team needs to do
        team.onAddPlayer(player)
    }

    fun TurfWarsGame.onRemovePlayerFromTeam(player: TurfPlayer, team: TurfWarsTeam) {
        // Do whatever the team needs to do
        team.onRemovePlayer(player)
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
        }
    }

    val gameScoreboardTitle by lazy { locale.scoreboardTitle() }

    fun TurfWarsGame.getScoreboardLines(player: TurfPlayer): List<Component> {
        val list = mutableListOf<ScoreboardElement>()

        // If the game is waiting for players, we should show the state and the amount of players
        if (state.showsStateInScoreboard()) {
            list += GroupScoreboardElement("Players") {
                locale.scoreboardPlayerCount(
                    playerCount,
                    max(config.game.maximumPlayers, playerCount)
                )
            }

            list += GroupScoreboardElement("State") { state.descriptionAsComponent(this) }
        }

        // If we are in game, we should show the player's team, the game time left,
        // and the time left for the current state
        if (state.isInGame()) {
            // Player's team
            player.team?.also {
                list += GroupScoreboardElement("Team") { it.name() }
            }

            // Game time left
            list += GroupScoreboardElement("Time Remaining") { timers.gameEndTimer.remainingTime() }

            // State time left
            val turfStateLogic = state.stateLogics.firstNotNullOfOrNull { it as? BaseTurfStateLogic }
            if (turfStateLogic != null) {
                val description = state.descriptionAsComponent(this)
                list += GroupScoreboardElement(
                    PlainTextComponentSerializer.plainText().serialize(
                        Component.join(
                            JoinConfiguration.separator(space()),
                            description,
                            Component.text("Time")
                        )
                    )
                ) { turfStateLogic.timer(this).remainingTime() }
            }
        }

        return list.map { it.toComponentList() }.joinTo(mutableListOf(), listOf(space())).flatten()
    }

}