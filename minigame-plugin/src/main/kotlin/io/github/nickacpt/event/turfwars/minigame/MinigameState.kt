package io.github.nickacpt.event.turfwars.minigame

import io.github.nickacpt.event.turfwars.minigame.logic.states.InitializingStateGameLogic
import io.github.nickacpt.event.turfwars.minigame.logic.states.StartingStateLogic
import io.github.nickacpt.event.turfwars.minigame.logic.states.TurfWarsGameStateLogic
import io.github.nickacpt.event.turfwars.minigame.logic.states.game.BuildStateLogic
import io.github.nickacpt.event.turfwars.minigame.logic.states.game.CombatStateLogic
import io.github.nickacpt.event.turfwars.minigame.logic.states.game.PlayerLocationSelectionStateLogic
import io.github.nickacpt.event.turfwars.minigame.logic.states.game.TeamSelectionStateLogic
import io.github.nickacpt.event.turfwars.minigame.logic.states.lobby.LobbyCountdownResetStateLogic
import io.github.nickacpt.event.turfwars.minigame.logic.states.lobby.PlayerWaitingStateGameLogic
import io.github.nickacpt.event.turfwars.minigame.timer.LobbyCountdownTimer
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder

enum class MinigameState(private val description: String, vararg val stateLogics: TurfWarsGameStateLogic) {
    INITIALIZING("<gray>Initializing..", InitializingStateGameLogic),
    WAITING("<yellow>Waiting for players..", PlayerWaitingStateGameLogic),

    STARTING("<green>Starting in <yellow><time></yellow>..", LobbyCountdownResetStateLogic, StartingStateLogic),
    TEAM_SELECTION("<gray>Team selection", LobbyCountdownResetStateLogic, TeamSelectionStateLogic),
    PLAYER_LOCATION_SELECTION("<gray>Player location selection", PlayerLocationSelectionStateLogic),

    TURF_BUILD("<yellow>Turf Build", BuildStateLogic),
    TURF_COMBAT("<yellow>Turf Combat", CombatStateLogic),
    ENDING("<red>Ending");

    companion object {
        fun waitingForPlayersStates() = arrayOf(WAITING, STARTING, TEAM_SELECTION)

        fun inGameStates() = arrayOf(TURF_BUILD, TURF_COMBAT)
    }

    /**
     * Whether players can join the game in this state
     */
    fun isWaitingForPlayers(): Boolean {
        return this in waitingForPlayersStates()
    }

    fun isInGame(): Boolean {
        return this in inGameStates()
    }

    fun showsStateInScoreboard(): Boolean {
        return isWaitingForPlayers() || this == ENDING
    }

    fun descriptionAsComponent(game: TurfWarsGame): Component {
        return MiniMessage.miniMessage().deserialize(
            description,
            Placeholder.component("time", LobbyCountdownTimer.formatTime(game.timers.lobbyCountdown.remainingTime))
        )
    }
}