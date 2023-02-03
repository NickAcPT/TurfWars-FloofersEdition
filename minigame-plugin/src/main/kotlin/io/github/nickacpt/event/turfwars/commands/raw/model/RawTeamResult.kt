package io.github.nickacpt.event.turfwars.commands.raw.model

data class RawTeamResult(
    val team: RawTeamInformation?,
    val inTeam: Boolean = team != null,
)