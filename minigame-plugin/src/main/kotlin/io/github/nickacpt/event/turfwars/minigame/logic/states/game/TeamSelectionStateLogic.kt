package io.github.nickacpt.event.turfwars.minigame.logic.states.game

import io.github.nickacpt.event.turfwars.TurfWarsPlugin
import io.github.nickacpt.event.turfwars.minigame.MinigameState
import io.github.nickacpt.event.turfwars.minigame.TurfWarsGame
import io.github.nickacpt.event.turfwars.minigame.logic.states.TurfWarsGameStateLogic
import io.github.nickacpt.event.turfwars.minigame.teams.TurfWarsTeam

object TeamSelectionStateLogic : TurfWarsGameStateLogic {
    override fun TurfWarsGame.tickState(): MinigameState {
        // Woah finally! It's about time we picked the teams!
        val shuffledPlayers = ArrayDeque(players.shuffled())

        // Now we have a shuffled list of players, so we need to find the playable teams
        // to sequentially add the players into them.
        val gameTeams = teams.filter { it.playable }.toMutableList()

        // We include the spectator team as a fallback for if there weren't any teams to fit the players into,
        // making sure it's the last team
        gameTeams += spectatorTeam

        // We have our teams, so now we can add the players, making sure we remove them off
        // the list of remaining players
        gameTeams.forEach { team ->
            while (shuffledPlayers.isNotEmpty() && (team.playerCount < TurfWarsTeam.maximumPlayerCount || !team.playable)) {
                val player = shuffledPlayers.removeFirst()
                team.addPlayer(player)

                val teamName = team.name()
                if (team.playable) {
                    TurfWarsPlugin.locale.teamSwitchNotification(player, teamName)
                } else {
                    TurfWarsPlugin.locale.unableToPlaceInTeam(player, teamName)
                }
            }
        }

        return MinigameState.PLAYER_LOCATION_SELECTION
    }
}