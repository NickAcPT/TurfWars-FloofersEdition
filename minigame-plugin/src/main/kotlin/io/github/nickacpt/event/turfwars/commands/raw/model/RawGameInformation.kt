package io.github.nickacpt.event.turfwars.commands.raw.model

import io.github.nickacpt.event.core.players.TurfPlayer
import io.github.nickacpt.event.turfwars.minigame.TurfWarsGame
import io.github.nickacpt.event.turfwars.minigame.teams.TurfWarsTeam.Companion.team
import java.util.*

data class RawGameInformation(
    val id: UUID,
    val state: RawMinigameState,
    val teams: List<RawTeamInformation>,
    val team: String? = null,
) {
    constructor(player: TurfPlayer, game: TurfWarsGame) : this(
        game.id, RawMinigameState(game.state, game),
        game.teams.map { RawTeamInformation(it) },
        player.team?.name
    )
}