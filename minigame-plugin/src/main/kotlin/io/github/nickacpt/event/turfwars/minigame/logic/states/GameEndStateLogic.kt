package io.github.nickacpt.event.turfwars.minigame.logic.states

import io.github.nickacpt.event.turfwars.minigame.MinigameState
import io.github.nickacpt.event.turfwars.minigame.logic.states.game.BaseTurfStateLogic

object GameEndStateLogic : BaseTurfStateLogic({ MinigameState.endingGameState() }, { timers.gameEndTimer })