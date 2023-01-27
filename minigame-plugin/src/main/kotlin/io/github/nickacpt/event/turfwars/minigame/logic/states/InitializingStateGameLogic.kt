package io.github.nickacpt.event.turfwars.minigame.logic.states

import io.github.nickacpt.event.turfwars.minigame.MinigameState
import io.github.nickacpt.event.turfwars.minigame.TurfWarsGame

object InitializingStateGameLogic : TurfWarsGameStateLogic {
    override fun TurfWarsGame.tickState(): MinigameState {
        // If we are in the initialization state, we should switch to the waiting state,
        // so we can wait for players to join.
        return MinigameState.WAITING
    }
}

