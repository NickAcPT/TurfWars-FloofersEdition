package io.github.nickacpt.event.turfwars.minigame.logic.states.game

import io.github.nickacpt.event.turfwars.TurfWarsPlugin
import io.github.nickacpt.event.turfwars.minigame.MinigameState
import io.github.nickacpt.event.turfwars.minigame.TurfWarsGame
import io.github.nickacpt.event.turfwars.minigame.logic.states.TurfWarsGameStateLogic
import io.github.nickacpt.event.turfwars.minigame.teams.TurfWarsTeam.Companion.team

object PlayerLocationSelectionStateLogic : TurfWarsGameStateLogic {
    override fun TurfWarsGame.tickState(): MinigameState {
        // Now that players have been placed into teams, we need to teleport them to their respective locations
        // by looping through all the players in the game and finding a location to teleport them into.
        players.forEach { player ->
            val team = player.team ?: return@forEach
            val bukkitPlayer = player.bukkitPlayer ?: return@forEach

            val spawn = team.spawnProvider(TurfWarsPlugin.config.game.playerSpawns)
            bukkitPlayer.teleport(spawn.toBukkitLocation(bukkitPlayer.world))
        }

        return MinigameState.TURF_BUILD
    }
}