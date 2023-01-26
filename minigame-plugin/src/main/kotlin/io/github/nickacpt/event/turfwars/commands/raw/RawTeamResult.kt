package io.github.nickacpt.event.turfwars.commands.raw

data class RawTeamResult(
    val team: RawTeamInformation?,
    val inTeam: Boolean = team != null,
)