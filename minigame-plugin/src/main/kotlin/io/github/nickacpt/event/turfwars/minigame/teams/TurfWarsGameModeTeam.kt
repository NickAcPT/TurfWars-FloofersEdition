package io.github.nickacpt.event.turfwars.minigame.teams

import io.github.nickacpt.event.core.players.TurfPlayer
import io.github.nickacpt.event.turfwars.config.ConfigurationLocation
import io.github.nickacpt.event.turfwars.config.PlayerSpawnsConfig
import io.github.nickacpt.event.turfwars.minigame.TurfWarsGame
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.GameMode

abstract class TurfWarsGameModeTeam(
    game: TurfWarsGame,
    name: String,
    color: NamedTextColor,
    spawnProvider: (PlayerSpawnsConfig) -> ConfigurationLocation,
    private val gameMode: GameMode
) : TurfWarsTeam(game, name, color, spawnProvider) {
    override fun onAddPlayer(player: TurfPlayer) {
        player.bukkitPlayer?.gameMode = gameMode
    }
}