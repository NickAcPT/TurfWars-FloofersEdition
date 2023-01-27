package io.github.nickacpt.event.turfwars.minigame.logic.states

import io.github.nickacpt.event.turfwars.minigame.MinigameState
import io.github.nickacpt.event.turfwars.minigame.TurfWarsGame

interface TurfWarsGameStateLogic {

    /**
     * Tick the current state.
     *
     * This method is called every tick (20 times per second) and is used
     * to update the state of the game and run any logic that is needed.
     *
     * @return The new state of the game, or null if the state should not change.
     */
    fun TurfWarsGame.tickState(): MinigameState?

}