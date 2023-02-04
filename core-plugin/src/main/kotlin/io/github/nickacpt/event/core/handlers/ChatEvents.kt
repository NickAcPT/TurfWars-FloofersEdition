package io.github.nickacpt.event.core.handlers

import io.github.nickacpt.event.core.CorePlugin
import io.papermc.paper.chat.ChatRenderer.viewerUnaware
import io.papermc.paper.event.player.AsyncChatEvent
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener

object ChatEvents : Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    fun onChat(e: AsyncChatEvent) {
        // Remove the console from the viewers, as we are going to handle it ourselves
        e.viewers().remove(Bukkit.getConsoleSender())

        e.renderer(viewerUnaware { source, _, message ->
            CorePlugin.instance.messages.chatMessage(source, message).also {
                Bukkit.getConsoleSender().sendMessage(it)
            }
        })
    }

}