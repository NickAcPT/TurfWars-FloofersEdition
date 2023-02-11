package io.github.nickacpt.event.core.commands

import cloud.commandframework.annotations.CommandMethod
import io.github.nickacpt.event.core.CorePlugin
import io.github.nickacpt.event.core.players.TurfPlayer
import io.github.nickacpt.event.core.players.TurfPlayerApiManager
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit

@CommandMethod("api")
object ApiCommand {

    @CommandMethod("new")
    fun newApiKey(sender: TurfPlayer) {
        Bukkit.getScheduler().runTaskAsynchronously(CorePlugin.instance, Runnable {
            sender.sendMessage(Component.text {
                val key = TurfPlayerApiManager.createNewApiKey(sender.data).toString()
                it.color(NamedTextColor.GREEN)
                it.append(Component.text("Your new API key is: "))
                it.append(Component.text(key).color(NamedTextColor.GOLD).clickEvent(ClickEvent.suggestCommand(key)))
            })
        })

    }
}