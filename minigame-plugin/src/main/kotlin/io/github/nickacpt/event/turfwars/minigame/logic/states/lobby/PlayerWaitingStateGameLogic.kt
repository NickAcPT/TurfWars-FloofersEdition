package io.github.nickacpt.event.turfwars.minigame.logic.states.lobby

import io.github.nickacpt.event.turfwars.minigame.MinigameState
import io.github.nickacpt.event.turfwars.minigame.TurfWarsGame

object PlayerWaitingStateGameLogic : MinimumPlayerCheckStateGameLogic() {
    override fun TurfWarsGame.tickState(): MinigameState? {
        // If we are in the waiting state, and we have more than the minimum players,
        // we should switch to the starting state and start the countdown.
        if (hasMinimumPlayersToStart) {
            timers.lobbyCountdown.restart()
            return MinigameState.STARTING
        }

        return null
    }
}