package io.github.nickacpt.event.turfwars.minigame.logic.states.game

import io.github.nickacpt.event.turfwars.minigame.MinigameState

object BuildStateLogic : BaseTurfStateLogic({ MinigameState.TURF_COMBAT }, { timers.buildCountdown })

