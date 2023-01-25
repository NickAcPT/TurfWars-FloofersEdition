package io.github.nickacpt.event.turfwars.utils

import io.github.nickacpt.event.turfwars.minigame.teams.TurfWarsTeam

class PrefixTeamConsoleProxyAudience(team: TurfWarsTeam) :
    PrefixedConsoleProxyAudience("[Game ${team.game.id} - Team ${team.name}]")