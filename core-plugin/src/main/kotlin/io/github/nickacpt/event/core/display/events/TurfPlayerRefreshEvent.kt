package io.github.nickacpt.event.core.display.events

import io.github.nickacpt.event.core.players.TurfPlayer
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

data class TurfPlayerRefreshEvent(val player: TurfPlayer) : Event() {
    override fun getHandlers(): HandlerList {
        return handlerList
    }

    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }
}