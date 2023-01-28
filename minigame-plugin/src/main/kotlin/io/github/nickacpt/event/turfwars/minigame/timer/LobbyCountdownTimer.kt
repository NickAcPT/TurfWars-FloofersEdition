package io.github.nickacpt.event.turfwars.minigame.timer

import io.github.nickacpt.event.turfwars.TurfWarsPlugin
import io.github.nickacpt.event.turfwars.TurfWarsPlugin.Companion.config
import io.github.nickacpt.event.turfwars.minigame.TurfWarsGame
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Sound

class LobbyCountdownTimer(game: TurfWarsGame) : CountdownTimer(game, config.game.lobbyCountdown) {

    companion object {
        fun formatTime(secondsLeft: Int): TextComponent {
            val shouldAlwaysShow = secondsLeft <= 5
            val color = when {
                shouldAlwaysShow -> NamedTextColor.RED
                secondsLeft <= 10 -> NamedTextColor.YELLOW
                else -> NamedTextColor.GREEN
            }

            return Component.text("$secondsLeft second${if (secondsLeft == 1) "" else "s"}", color)
        }
    }

    override fun onTick(secondsLeft: Int) {
        game.debug("Lobby countdown timer ticked, $secondsLeft seconds left")
        game.refreshPlayers()

        val shouldAlwaysShow = secondsLeft <= 5
        val shouldShow = shouldAlwaysShow || secondsLeft % 5 == 0
        if (!shouldShow) return

        // Play a ticking sound
        game.playSound(
            net.kyori.adventure.sound.Sound.sound(
                Sound.BLOCK_NOTE_BLOCK_HAT,
                net.kyori.adventure.sound.Sound.Source.RECORD,
                1f,
                1f
            )
        )

        // Send a message to all players
        TurfWarsPlugin.locale.countdownTimerMessage(
            game,
            formatTime(secondsLeft)
        )
    }
}