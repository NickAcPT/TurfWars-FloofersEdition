package io.github.nickacpt.event.turfwars.minigame.teams

import io.github.nickacpt.event.core.players.TurfPlayer
import io.github.nickacpt.event.core.players.TurfPlayerDataKey
import io.github.nickacpt.event.turfwars.TurfWarsPlugin
import io.github.nickacpt.event.turfwars.TurfWarsPlugin.Companion.locale
import io.github.nickacpt.event.turfwars.minigame.TurfWarsGame
import io.github.nickacpt.event.turfwars.utils.PrefixTeamConsoleProxyAudience
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.audience.ForwardingAudience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import java.util.*

data class TurfWarsTeam(
    val game: TurfWarsGame,
    val name: String,
    val color: NamedTextColor,
    val playable: Boolean = true,
) : ForwardingAudience {

    companion object {
        // Since TurfWars is known to have only two teams, we can evenly split the max count in 2
        val maximumPlayerCount get() = TurfWarsPlugin.config.game.maximumPlayers / 2

        private val TEAM_TAG = TurfPlayerDataKey<TurfWarsTeam>()

        var TurfPlayer.team
            get() = this[TEAM_TAG]
            set(value) {
                this[TEAM_TAG] = value
                this.refresh()
            }
    }

    val players = mutableMapOf<UUID, TurfPlayer>()
    val playerCount get() = players.size

    private val consoleAudience = PrefixTeamConsoleProxyAudience(this)

    fun name(): Component = Component.text {
        it.append(Component.text(name))
        it.appendSpace()
        it.append(Component.text("Team"))
        it.color(color)
    }

    fun addPlayer(player: TurfPlayer) {
        // Remove player from previous team
        player.team?.removePlayer(player)

        // Add player to this team
        players[player.uuid] = player
        player.team = this

        debug("Player ${player.name} is now part of the team.")
    }

    fun removePlayer(player: TurfPlayer) {
        players.remove(player.uuid)
        player.team = null

        debug("Player ${player.name} is no longer part of the team.")
    }

    inline fun debug(message: String) {
        locale.debug(this, message)
    }

    override fun audiences(): Iterable<Audience> {
        return listOf(*players.values.toTypedArray(), consoleAudience).toList()
    }

}
