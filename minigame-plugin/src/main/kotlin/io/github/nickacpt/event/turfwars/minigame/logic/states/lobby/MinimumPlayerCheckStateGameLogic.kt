package io.github.nickacpt.event.turfwars.minigame.logic.states.lobby

import io.github.nickacpt.event.turfwars.TurfWarsPlugin
import io.github.nickacpt.event.turfwars.minigame.TurfWarsGame
import io.github.nickacpt.event.turfwars.minigame.logic.states.TurfWarsGameStateLogic

abstract class MinimumPlayerCheckStateGameLogic : TurfWarsGameStateLogic {
    // Whether we have enough players to start a game.
    val TurfWarsGame.hasMinimumPlayersToStart get() = forceStart || playerCount >= TurfWarsPlugin.config.game.minimumPlayers
}