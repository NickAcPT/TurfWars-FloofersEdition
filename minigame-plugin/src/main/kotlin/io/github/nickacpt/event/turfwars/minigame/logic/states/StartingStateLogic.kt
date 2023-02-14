package io.github.nickacpt.event.turfwars.minigame.logic.states

import io.github.nickacpt.event.turfwars.minigame.MinigameState
import io.github.nickacpt.event.turfwars.minigame.TurfWarsGame

object StartingStateLogic : TurfWarsGameStateLogic {
    override fun TurfWarsGame.tickState(): MinigameState? {
        // If we are in the starting state, the countdown has finished, or we are supposed to force start the game,
        // we should first stop the lobby timer, start the game timer and switch to the first in-game state: Team Selection
        if (this.forceStart || timers.lobbyCountdown.isFinished) {
            timers.lobbyCountdown.stop()
            timers.gameEndTimer.restart()
            return MinigameState.firstGameStartState()
        }

        return null
    }
}