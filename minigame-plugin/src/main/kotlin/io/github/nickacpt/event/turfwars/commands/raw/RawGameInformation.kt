package io.github.nickacpt.event.turfwars.commands.raw

import io.github.nickacpt.event.core.players.TurfPlayer
import io.github.nickacpt.event.turfwars.minigame.MinigameState
import io.github.nickacpt.event.turfwars.minigame.TurfWarsGame
import io.github.nickacpt.event.turfwars.minigame.teams.TurfWarsTeam.Companion.team
import java.util.*

data class RawGameInformation(
    val id: UUID,
    val state: MinigameState,
    val teams: List<RawTeamInformation>,
    val team: String? = null,
) {
    constructor(player: TurfPlayer, game: TurfWarsGame) : this(
        game.id, game.state, game.teams.map { RawTeamInformation(it) },
        player.team?.name
    )
}