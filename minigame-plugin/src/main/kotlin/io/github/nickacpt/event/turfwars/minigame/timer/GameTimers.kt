package io.github.nickacpt.event.turfwars.minigame.timer

import io.github.nickacpt.event.turfwars.minigame.TurfWarsGame
import io.github.nickacpt.event.turfwars.minigame.logic.LobbyCountdownTimer

data class GameTimers(
    val game: TurfWarsGame
) {
    val lobbyCountdown = LobbyCountdownTimer(game)

    private val timersList = listOf<CountdownTimer>(lobbyCountdown)

    /**
     * Ticks all the timers in the list, 20 times per second
     */
    fun tick() {
        timersList.forEach { it.tick() }
    }
}
