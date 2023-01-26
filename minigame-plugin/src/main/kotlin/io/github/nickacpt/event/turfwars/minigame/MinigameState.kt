package io.github.nickacpt.event.turfwars.minigame

import io.github.nickacpt.event.turfwars.minigame.logic.LobbyCountdownTimer
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder

enum class MinigameState(private val description: String) {
    INITIALIZING("<gray>Initializing.."),
    WAITING("<yellow>Waiting for players.."),
    STARTING("<green>Starting in <yellow><time></yellow>.."),
    TEAM_SELECTION("<gray>Team selection"),
    IN_GAME("<gold>Game in progress"),
    ENDING("<red>Ending");

    /**
     * Whether players can join the game in this state
     */
    fun isWaitingForPlayers(): Boolean {
        return this == WAITING || this == STARTING
    }

    fun isInGame(): Boolean {
        return this == TEAM_SELECTION || this == IN_GAME
    }

    fun showsStateInScoreboard(): Boolean {
        return isWaitingForPlayers() || this == ENDING
    }

    fun descriptionAsComponent(game: TurfWarsGame): Component {
        return MiniMessage.miniMessage().deserialize(
            description,
            Placeholder.component("time", LobbyCountdownTimer.formatTime(game.timers.lobbyCountdown.remainingTime))
        )
    }

}