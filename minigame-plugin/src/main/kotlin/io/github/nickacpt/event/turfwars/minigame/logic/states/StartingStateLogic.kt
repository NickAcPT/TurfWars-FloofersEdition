package io.github.nickacpt.event.turfwars.minigame.logic.states

import io.github.nickacpt.event.turfwars.minigame.MinigameState
import io.github.nickacpt.event.turfwars.minigame.TurfWarsGame

object StartingStateLogic : TurfWarsGameStateLogic {
    override fun TurfWarsGame.tickState(): MinigameState? {
        // If we are in the starting state, and the countdown has finished,
        // we should switch to the first in-game state: Team Selection.
        if (timers.lobbyCountdown.isFinished) {
            return MinigameState.firstGameStartState()
        }

        return null
    }
}