package io.github.nickacpt.event.core.handlers

import io.github.nickacpt.event.core.CorePlugin
import io.papermc.paper.chat.ChatRenderer.viewerUnaware
import io.papermc.paper.event.player.AsyncChatEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

object ChatEvents : Listener {

    @EventHandler
    fun onChat(e: AsyncChatEvent) {
        e.renderer(viewerUnaware { source, _, message ->
            CorePlugin.instance.messages.chatMessage(source, message)
        })
    }

}