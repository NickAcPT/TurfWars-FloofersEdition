package io.github.nickacpt.event.turfwars.minigame.timer

import io.github.nickacpt.event.turfwars.minigame.TurfWarsGame

open class PlayerRefreshingCountdownTimer(game: TurfWarsGame, totalSeconds: Int) : CountdownTimer(game, totalSeconds) {
    override fun onTick(secondsLeft: Int) {
        game.refreshPlayers()
    }

    override fun onFinish() {
        game.refreshPlayers()
    }
}