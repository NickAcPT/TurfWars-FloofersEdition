package io.github.nickacpt.event.turfwars.minigame.logic.states.game

import io.github.nickacpt.event.turfwars.minigame.MinigameState

object CombatStateLogic : BaseTurfStateLogic({ MinigameState.TURF_BUILD }, { timers.combatCountdown })