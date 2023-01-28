package io.github.nickacpt.event.turfwars.minigame.timer

import io.github.nickacpt.event.turfwars.TurfWarsPlugin
import io.github.nickacpt.event.turfwars.minigame.TurfWarsGame

data class GameTimers(
    val game: TurfWarsGame
) {
    val lobbyCountdown = LobbyCountdownTimer(game)
    val buildCountdown = PlayerRefreshingCountdownTimer(game, TurfWarsPlugin.config.game.buildTime)
    val combatCountdown = PlayerRefreshingCountdownTimer(game, TurfWarsPlugin.config.game.combatTime)

    private val timersList = listOf(
        lobbyCountdown,
        buildCountdown,
        combatCountdown
    )

    /**
     * Ticks all the timers in the list, 20 times per second
     */
    fun tick() {
        timersList.forEach { it.tick() }
    }
}
