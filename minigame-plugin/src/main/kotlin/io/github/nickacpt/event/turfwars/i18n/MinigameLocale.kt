package io.github.nickacpt.event.turfwars.i18n

import io.github.nickacpt.event.core.players.TurfPlayer
import io.github.nickacpt.event.turfwars.minigame.TurfWarsGame
import io.github.nickacpt.event.utils.messages.annotations.ActionBar
import io.github.nickacpt.event.utils.messages.annotations.Receiver
import net.kyori.adventure.text.Component
import net.kyori.moonshine.annotation.Message
import net.kyori.moonshine.annotation.Placeholder

interface MinigameLocale {

    @Message("minigame.join")
    fun joinedGame(
        @Receiver game: TurfWarsGame,
        @Placeholder("player") player: TurfPlayer
    )

    @Message("minigame.quit")
    fun quitGame(
        @Receiver game: TurfWarsGame,
        @Placeholder("player") player: TurfPlayer
    )

    @Message("minigame.start-countdown")
    fun countdownTimerMessage(
        @Receiver game: TurfWarsGame,
        @Placeholder("time") time: Component
    )

    @Message("minigame.not-enough-players")
    fun countdownTimerCancelled(
        @Receiver game: TurfWarsGame
    )

    @Message("minigame.scoreboard-title")
    fun scoreboardTitle(): Component

    @ActionBar
    @Message("minigame.debug")
    fun debug(
        @Receiver game: TurfWarsGame,
        @Placeholder("message") message: String
    )

}