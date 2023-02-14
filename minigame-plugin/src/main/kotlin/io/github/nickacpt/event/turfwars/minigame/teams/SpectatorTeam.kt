package io.github.nickacpt.event.turfwars.minigame.teams

import io.github.nickacpt.event.turfwars.minigame.TurfWarsGame
import io.github.nickacpt.event.turfwars.minigame.timer.CountdownTimer
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.GameMode
import java.util.*

class SpectatorTeam(game: TurfWarsGame) : TurfWarsGameModeTeam(
    game, "Spectator", NamedTextColor.GRAY, { it.spectator },
    GameMode.SPECTATOR
) {
    private val playerTimers = mutableMapOf<UUID, CountdownTimer>()

    override val playable: Boolean
        get() = false
}