package io.github.nickacpt.event.turfwars.minigame.logic.states.lobby

import io.github.nickacpt.event.turfwars.TurfWarsPlugin
import io.github.nickacpt.event.turfwars.minigame.MinigameState
import io.github.nickacpt.event.turfwars.minigame.TurfWarsGame

object LobbyCountdownResetStateLogic : MinimumPlayerCheckStateGameLogic() {
    override fun TurfWarsGame.tickState(): MinigameState? {
        // If we are in a state where players can still join into teams,
        // and we have less than the minimum players, we should switch to the waiting state.
        if (!hasMinimumPlayersToStart) {
            // If we are in the starting state, we should cancel the countdown and notify the players.
            if (timers.lobbyCountdown.isRunning) {
                timers.lobbyCountdown.stop()
                TurfWarsPlugin.locale.countdownTimerCancelled(this)
            }

            // And now we wait!
            return MinigameState.WAITING
        }

        return null
    }
}