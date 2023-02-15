package io.github.nickacpt.event.turfwars.minigame.timer

import io.github.nickacpt.event.turfwars.TurfWarsPlugin
import io.github.nickacpt.event.turfwars.minigame.TurfWarsGame

data class GameTimers(
    val game: TurfWarsGame
) {
    val lobbyCountdown = LobbyCountdownTimer(game)
    val buildCountdown = PlayerRefreshingCountdownTimer(game, TurfWarsPlugin.config.game.buildTime)
    val combatCountdown = PlayerRefreshingCountdownTimer(game, TurfWarsPlugin.config.game.combatTime)
    val gameEndTimer = CountdownTimer(game, TurfWarsPlugin.config.game.endTime)

    private val timersList = listOf(
        lobbyCountdown,
        buildCountdown,
        combatCountdown,
        gameEndTimer
    )

    /**
     * Ticks all the timers in the list, 1 time per second
     */
    fun tick() {
        timersList.forEach { it.tick() }
    }
}
