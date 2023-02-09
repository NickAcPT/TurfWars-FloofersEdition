package io.github.nickacpt.event.turfwars.minigame.teams

import io.github.nickacpt.event.turfwars.config.ConfigurationLocation
import io.github.nickacpt.event.turfwars.config.PlayerSpawnsConfig
import io.github.nickacpt.event.turfwars.minigame.TurfWarsGame
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.GameMode
import java.util.*

class TurfWarsPlayerTeam(
    game: TurfWarsGame,
    color: NamedTextColor,
    spawnProvider: (PlayerSpawnsConfig) -> ConfigurationLocation
) : TurfWarsGameModeTeam(game,
    color.toString().replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
    color,
    spawnProvider,
    GameMode.ADVENTURE
) {
    override val playable: Boolean
        get() = true
}