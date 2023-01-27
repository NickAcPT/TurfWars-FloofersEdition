package io.github.nickacpt.event.turfwars.commands.raw

import io.github.nickacpt.event.turfwars.minigame.teams.TurfWarsTeam
import java.util.*

data class RawTeamInformation(
    val name: String,
    val color: String,
    val players: Set<UUID>
) {
    constructor(team: TurfWarsTeam) : this(team.name, team.color.toString(), team.players.map { it.uuid }.toSet())
}