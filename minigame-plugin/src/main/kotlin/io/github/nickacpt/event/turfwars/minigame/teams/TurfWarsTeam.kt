package io.github.nickacpt.event.turfwars.minigame.teams

import io.github.nickacpt.event.core.players.TurfPlayer
import io.github.nickacpt.event.core.players.TurfPlayerDataKey
import io.github.nickacpt.event.turfwars.minigame.TurfWarsGame
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.audience.ForwardingAudience
import net.kyori.adventure.text.format.NamedTextColor

data class TurfWarsTeam(
    val game: TurfWarsGame,
    val name: String,
    val color: NamedTextColor,
    val playable: Boolean = true,
) : ForwardingAudience {
    val players = mutableListOf<TurfPlayer>()
    val playerCount get() = players.size

    companion object {
        private val TEAM_TAG = TurfPlayerDataKey<TurfWarsTeam>()

        var TurfPlayer.team
            get() = this[TEAM_TAG]
            set(value) {
                this[TEAM_TAG] = value
            }
    }

    fun addPlayer(player: TurfPlayer) {
        // Remove player from previous team
        player.team?.removePlayer(player)

        // Add player to this team
        players.add(player)
        player.team = this
    }

    fun removePlayer(player: TurfPlayer) {
        players.remove(player)
        player.team = null
    }

    override fun audiences(): MutableIterable<Audience> {
        return players
    }

}
