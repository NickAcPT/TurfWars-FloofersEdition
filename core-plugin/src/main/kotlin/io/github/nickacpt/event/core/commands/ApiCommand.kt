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
    suspend fun newApiKey(sender: TurfPlayer) {
        val key = TurfPlayerApiManager.createNewApiKey(sender.data).toString()

        sender.sendMessage(Component.text {
            it.color(NamedTextColor.GREEN)
            it.append(Component.text("Your new API key is: "))
            it.append(Component.text(key).color(NamedTextColor.GOLD).clickEvent(ClickEvent.suggestCommand(key)))
        })

    }
}