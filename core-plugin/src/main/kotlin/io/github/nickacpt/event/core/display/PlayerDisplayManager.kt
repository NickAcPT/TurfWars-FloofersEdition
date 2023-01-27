package io.github.nickacpt.event.core.display

import io.github.nickacpt.event.core.display.events.TurfPlayerRefreshEvent
import io.github.nickacpt.event.core.display.providers.PlayerTagProvider
import io.github.nickacpt.event.core.display.scoreboard.PlayerScoreboardDisplayData
import io.github.nickacpt.event.core.display.scoreboard.SidebarManager
import io.github.nickacpt.event.core.players.TurfPlayer
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
        providers.add(0, provider)
    }

    @EventHandler
    fun onPlayerJoin(e: PlayerJoinEvent) {
        e.player.turfPlayer.initializeBukkitScoreboard()
    }

    @EventHandler
    fun onPlayerRefresh(e: TurfPlayerRefreshEvent) {
        val dataProviderStack = ArrayDeque(providers)
        var data: PlayerDisplayData?

        do {
            data = dataProviderStack.removeFirstOrNull()?.provideDisplay(e.player)
        } while (data == null && dataProviderStack.size != 0)

        updatePlayerScoreboard(e.player)

        Bukkit.getOnlinePlayers().forEach { receiver ->
            receiver.turfPlayer.bukkitScoreboard?.let { data?.applyToScoreboard(it, e.player) }
        }
    }

    private fun updatePlayerScoreboard(player: TurfPlayer) {
        val scoreboardProviderStack = ArrayDeque(providers)

        // Compute the player's scoreboard data
        var score: PlayerScoreboardDisplayData?
        do {
            score = scoreboardProviderStack.removeFirstOrNull()?.provideScoreboardDisplay(player)
        } while (score == null && scoreboardProviderStack.size != 0)

        // Update the player's tracked scoreboard data
        score?.let {
            player.trackingSidebarData.title = it.title
            player.trackingSidebarData.lines = it.lines
        }

        SidebarManager.notifySidebarDisplay(player, player.trackingSidebarData)
    }
}