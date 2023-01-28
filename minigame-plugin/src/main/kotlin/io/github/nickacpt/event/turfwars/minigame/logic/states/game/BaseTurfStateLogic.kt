package io.github.nickacpt.event.turfwars.minigame.logic.states.game

import io.github.nickacpt.event.turfwars.minigame.MinigameState
import io.github.nickacpt.event.turfwars.minigame.TurfWarsGame
import io.github.nickacpt.event.turfwars.minigame.logic.states.TurfWarsGameStateLogic
import io.github.nickacpt.event.turfwars.minigame.timer.CountdownTimer

open class BaseTurfStateLogic(private val nextState: () -> MinigameState, val timer: TurfWarsGame.() -> CountdownTimer) :
    TurfWarsGameStateLogic {
    override fun TurfWarsGame.tickState(): MinigameState? {
        // If the time of the current state has ended, we should switch to the next state.
        if (timer().isFinished) {
            timer().reset()
            return nextState()
        }

        // If we didn't start the countdown timer, then we'll have to
        // restart it and wait until it's over to switch to the next state
        if (!timer().isRunning) {
            timer().restart()
            refreshPlayers()
        }

        return null
    }
}