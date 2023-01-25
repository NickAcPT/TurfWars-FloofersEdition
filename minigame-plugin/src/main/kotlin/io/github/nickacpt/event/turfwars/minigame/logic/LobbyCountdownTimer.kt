package io.github.nickacpt.event.turfwars.minigame.logic

import io.github.nickacpt.event.turfwars.TurfWarsPlugin
import io.github.nickacpt.event.turfwars.minigame.TurfWarsGame
import io.github.nickacpt.event.turfwars.minigame.timer.CountdownTimer
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Sound as BukkitSound

class LobbyCountdownTimer(game: TurfWarsGame) : CountdownTimer(game, TurfWarsPlugin.config.game.lobbyCountdown) {
    override fun onTick(secondsLeft: Int) {
        game.debug("Lobby countdown timer ticked, $secondsLeft seconds left")

        val shouldAlwaysShow = secondsLeft <= 5
        val shouldShow = shouldAlwaysShow || secondsLeft % 5 == 0
        val color = when {
            shouldAlwaysShow -> NamedTextColor.RED
            secondsLeft <= 10 -> NamedTextColor.YELLOW
            else -> NamedTextColor.GREEN
        }

        if (!shouldShow) return

        // Play a ticking sound
        game.playSound(Sound.sound(BukkitSound.BLOCK_NOTE_BLOCK_HAT, Sound.Source.RECORD, 1f, 1f))

        // Send a message to all players
        TurfWarsPlugin.locale.countdownTimerMessage(
            game,
            Component.text("$secondsLeft second${if (secondsLeft == 1) "" else "s"}", color)
        )
    }
}