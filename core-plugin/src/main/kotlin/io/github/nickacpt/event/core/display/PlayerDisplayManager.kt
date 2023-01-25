package io.github.nickacpt.event.core.display

import io.github.nickacpt.event.core.display.events.TurfPlayerRefreshEvent
import io.github.nickacpt.event.core.display.providers.PlayerTagProvider
import io.github.nickacpt.event.core.display.scoreboard.PlayerScoreboardDisplayData
import io.github.nickacpt.event.utils.turfPlayer
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

object PlayerDisplayManager : Listener {
    private val providers = mutableListOf<PlayerDisplayProvider>(
        PlayerTagProvider,
    )

    fun registerProvider(provider: PlayerDisplayProvider) {
        providers.add(provider)
    }

    @EventHandler
    fun onPlayerJoin(e: PlayerJoinEvent) {
        e.player.turfPlayer.initializeBukkitScoreboard()
    }

    @EventHandler
    fun onPlayerRefresh(e: TurfPlayerRefreshEvent) {
        val dataProviderStack = ArrayDeque(providers)
        val scoreboardProviderStack = ArrayDeque(providers)
        var data: PlayerDisplayData?
        var score: PlayerScoreboardDisplayData?

        do {
            data = dataProviderStack.removeFirstOrNull()?.provideDisplay(e.player)
        } while (data == null && dataProviderStack.size != 0)

        do {
            score = scoreboardProviderStack.removeFirstOrNull()?.provideScoreboardDisplay(e.player)
        } while (score == null && scoreboardProviderStack.size != 0)

        e.player.bukkitScoreboard?.let { score?.applyToScoreboard(e.player) }

        Bukkit.getOnlinePlayers().forEach { receiver ->
            receiver.turfPlayer.bukkitScoreboard?.let { data?.applyToScoreboard(it, e.player) }
        }
    }
}