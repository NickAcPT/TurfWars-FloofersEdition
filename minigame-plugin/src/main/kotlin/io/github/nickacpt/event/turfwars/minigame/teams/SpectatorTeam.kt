package io.github.nickacpt.event.turfwars.minigame.teams

import io.github.nickacpt.event.turfwars.minigame.TurfWarsGame
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.GameMode

class SpectatorTeam(game: TurfWarsGame) : TurfWarsGameModeTeam(
    game, "Spectator", NamedTextColor.GRAY, { it.spectator },
    GameMode.SPECTATOR
) {
    override val playable: Boolean
        get() = false
}