package io.github.nickacpt.event.core.display

import io.github.nickacpt.event.core.display.events.TurfPlayerRefreshEvent
import io.github.nickacpt.event.core.display.providers.PlayerTagProvider
import io.github.nickacpt.event.utils.turfPlayer
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

object PlayerDisplayManager : Listener {
    private val providers = mutableListOf<PlayerDisplayProvider>(
        PlayerTagProvider,
    )

    @EventHandler
    fun onPlayerJoin(e: PlayerJoinEvent) {
        e.player.turfPlayer.initializeBukkitScoreboard()
    }

    @EventHandler
    fun onPlayerRefresh(e: TurfPlayerRefreshEvent) {
        val providerStack = ArrayDeque(providers)
        var data: PlayerDisplayData?

        do {
            data = providerStack.removeFirstOrNull()?.provideDisplay(e.player)
        } while (data == null && providerStack.size != 0)

        Bukkit.getOnlinePlayers().forEach { receiver ->
            receiver.turfPlayer.bukkitScoreboard?.let { data?.applyToScoreboard(it, e.player) }
        }
    }
}