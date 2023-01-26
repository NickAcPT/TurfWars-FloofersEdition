package io.github.nickacpt.event.turfwars.minigame.logic

import io.github.nickacpt.event.core.players.TurfPlayer
import io.github.nickacpt.event.turfwars.TurfWarsPlugin.Companion.config
import io.github.nickacpt.event.turfwars.TurfWarsPlugin.Companion.locale
import io.github.nickacpt.event.turfwars.minigame.MinigameState
import io.github.nickacpt.event.turfwars.minigame.TurfWarsGame
import io.github.nickacpt.event.turfwars.minigame.logic.LobbyCountdownTimer.Companion.formatTime
import io.github.nickacpt.event.utils.joinTo
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.space
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder

object TurfWarsLogic {

    private fun TurfWarsGame.moveToNextState(previousState: MinigameState): MinigameState? {
        // If we are in the initialization state, we should switch to the waiting state,
        // so we can wait for players to join
        if (previousState == MinigameState.INITIALIZING) {
            return MinigameState.WAITING
        }

        // Whether we have enough players to start a game
        val hasMinimumPlayersToStart = playerCount >= config.game.minimumPlayers

        // If we are in a state where players can still join into teams,
        // and we have less than the minimum players, we should switch to the waiting state
        if (previousState.isWaitingForPlayers() && !hasMinimumPlayersToStart) {
            // If we are in the starting state, we should cancel the countdown and notify the players.
            if (timers.lobbyCountdown.isRunning) {
                timers.lobbyCountdown.stop()
                locale.countdownTimerCancelled(this)
            }

            // And now we wait!
            return MinigameState.WAITING
        }

        // If we are in the waiting state, and we have more than the minimum players,
        // we should switch to the starting state and start the countdown
        if (previousState == MinigameState.WAITING && hasMinimumPlayersToStart) {
            timers.lobbyCountdown.restart()
            return MinigameState.STARTING
        }

        // If we are in the starting state, and the countdown has finished,
        // we should switch to the in-game state
        if (previousState == MinigameState.STARTING && timers.lobbyCountdown.hasFinished) {
            return MinigameState.IN_GAME
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

    fun TurfWarsGame.getScoreboardLines(player: TurfPlayer): List<Component>? {
        // If the game is waiting for players, we should show the state and the amount of players
        val list = mutableListOf<List<Component>>()

        if (state.showsStateInScoreboard()) {
            list += listOf(
                Component.text("Players", null, TextDecoration.BOLD),
                locale.scoreboardPlayerCount(playerCount, config.game.maximumPlayers)
            )

            list += listOf(
                Component.text("State", null, TextDecoration.BOLD),
                MiniMessage.miniMessage().deserialize(
                    state.description,
                    Placeholder.component("time", formatTime(timers.lobbyCountdown.remainingTime))
                )
            )
        }

        return list.takeIf { it.isNotEmpty() }?.joinTo(mutableListOf(), listOf(space()))?.flatten()
    }

}