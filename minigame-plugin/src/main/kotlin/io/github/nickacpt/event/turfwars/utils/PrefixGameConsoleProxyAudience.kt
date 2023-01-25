package io.github.nickacpt.event.turfwars.utils

import io.github.nickacpt.event.turfwars.minigame.TurfWarsGame

class PrefixGameConsoleProxyAudience(game: TurfWarsGame) : PrefixedConsoleProxyAudience("[Game ${game.id}]")