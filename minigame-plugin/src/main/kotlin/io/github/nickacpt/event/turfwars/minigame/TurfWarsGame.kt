package io.github.nickacpt.event.turfwars.minigame

import io.github.nickacpt.event.core.players.TurfPlayer
import io.github.nickacpt.event.core.players.TurfPlayerDataKey
import io.github.nickacpt.event.turfwars.TurfWarsPlugin.Companion.locale
import io.github.nickacpt.event.turfwars.minigame.logic.TurfWarsLogic
import io.github.nickacpt.event.turfwars.minigame.teams.TurfWarsTeam
import io.github.nickacpt.event.turfwars.minigame.teams.TurfWarsTeam.Companion.team
import io.github.nickacpt.event.turfwars.minigame.timer.GameTimers
import io.github.nickacpt.event.turfwars.utils.PrefixGameConsoleProxyAudience
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.audience.ForwardingAudience
import net.kyori.adventure.text.format.NamedTextColor
import java.util.*

data class TurfWarsGame internal constructor(
    val id: UUID,
) : ForwardingAudience {

    companion object {
        private val GAME_TAG = TurfPlayerDataKey<TurfWarsGame>()

        var TurfPlayer.game
            get() = this[GAME_TAG]
            set(value) {
                this[GAME_TAG] = value
                this.refresh()
            }
    }

    private val consoleAudience = PrefixGameConsoleProxyAudience(this)

    private val playersMap = mutableMapOf<UUID, TurfPlayer>()
    val players: Collection<TurfPlayer> get() = playersMap.values

    val playerCount get() = playersMap.values.count { it.team != spectatorTeam }
    val timers = GameTimers(this)

    private val spectatorTeam = TurfWarsTeam(this, "Spectator", NamedTextColor.GRAY, playable = false)
    val teams = mutableListOf(
        spectatorTeam,
        TurfWarsTeam(this, "Red", NamedTextColor.RED),
        TurfWarsTeam(this, "Blue", NamedTextColor.BLUE),
    )

    var state: MinigameState = MinigameState.INITIALIZING
        set(value) {
            field = value
            debug("Game state changed to $value")
        }

    fun addPlayer(player: TurfPlayer) {
        // Remove player from previous game
        player.game?.removePlayer(player)

        // Add player to this game
        playersMap[player.uuid] = player
        player.game = this

        locale.joinedGame(this, player)
    }

    fun removePlayer(player: TurfPlayer) {
        // Remove player from their team
        player.team?.removePlayer(player)

        // Then remove player from the game
        playersMap.remove(player.uuid)
        player.game = null

        locale.quitGame(this, player)
    }

    /**
     * Called every tick (20 times per second)
     */
    fun tick() {
        // Tick all the timers
        timers.tick()

        // Tick game logic
        with(TurfWarsLogic) {
            tickGame()
        }
    }

    inline fun debug(message: String) {
        locale.debug(this, message)
    }

    override fun audiences(): Iterable<Audience> {
        return listOf(*playersMap.values.toTypedArray(), consoleAudience)
    }
}