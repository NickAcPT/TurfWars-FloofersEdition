package io.github.nickacpt.event.turfwars.minigame.teams

import io.github.nickacpt.event.core.players.TurfPlayer
import io.github.nickacpt.event.core.players.TurfPlayerDataKey
import io.github.nickacpt.event.turfwars.TurfWarsPlugin
import io.github.nickacpt.event.turfwars.TurfWarsPlugin.Companion.locale
import io.github.nickacpt.event.turfwars.config.ConfigurationLocation
import io.github.nickacpt.event.turfwars.config.PlayerSpawnsConfig
import io.github.nickacpt.event.turfwars.minigame.TurfWarsGame
import io.github.nickacpt.event.turfwars.minigame.logic.TurfWarsLogic
import io.github.nickacpt.event.turfwars.utils.PrefixTeamConsoleProxyAudience
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.audience.ForwardingAudience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Location
import java.util.*

abstract class TurfWarsTeam(
    val game: TurfWarsGame,
    val name: String,
    val color: NamedTextColor,
    val spawnProvider: (PlayerSpawnsConfig) -> ConfigurationLocation
) : ForwardingAudience {
    abstract val playable: Boolean

    companion object {
        // Since TurfWars is known to have only two teams, we can evenly split the max count in 2
        val maximumPlayerCount get() = TurfWarsPlugin.config.game.maximumPlayers / 2

        private val TEAM_TAG = TurfPlayerDataKey<TurfWarsTeam>()
        private val PLAYABLE_TEAM_TAG = TurfPlayerDataKey<TurfWarsTeam>()

        internal val TurfPlayer.playableTeam get() = this[PLAYABLE_TEAM_TAG]

        var TurfPlayer.team
            get() = this[TEAM_TAG]
            set(value) {
                this[TEAM_TAG] = value
                if (value?.playable == true) this[PLAYABLE_TEAM_TAG] = value
                this.refresh()
            }
    }

    private val playerPlacedBlocksSet = mutableSetOf<Location>()

    fun addPlayerPlacedBlock(location: Location) {
        playerPlacedBlocksSet.add(location)
    }

    fun removePlayerPlacedBlock(location: Location) {
        playerPlacedBlocksSet.remove(location)
    }

    fun isBlockPlacedByPlayer(location: Location) = playerPlacedBlocksSet.contains(location)


    private val playersMap = mutableMapOf<UUID, TurfPlayer>()
    val players get() = playersMap.values

    val playerCount get() = playersMap.size

    private val consoleAudience = PrefixTeamConsoleProxyAudience(this)

    fun name(): Component = Component.text {
        it.append(Component.text(name))
        it.appendSpace()
        it.append(Component.text("Team"))
        it.color(color)
    }

    open fun onAddPlayer(player: TurfPlayer) {}

    open fun onRemovePlayer(player: TurfPlayer) {}

    fun addPlayer(player: TurfPlayer) {
        // Remove player from previous team
        player.team?.removePlayer(player)

        // Add player to this team
        playersMap[player.uuid] = player
        player.team = this

        debug("Player ${player.name} is now part of the team.")
        with(TurfWarsLogic) { game.onAddPlayerToTeam(player, this@TurfWarsTeam) }
    }

    fun removePlayer(player: TurfPlayer) {
        playersMap.remove(player.uuid)
        player.team = null

        debug("Player ${player.name} is no longer part of the team.")
        with(TurfWarsLogic) { game.onRemovePlayerFromTeam(player, this@TurfWarsTeam) }
    }

    inline fun debug(message: String) {
        locale.debug(this, message)
    }

    override fun audiences(): Iterable<Audience> {
        return listOf(*players.toTypedArray(), consoleAudience)
    }

}
