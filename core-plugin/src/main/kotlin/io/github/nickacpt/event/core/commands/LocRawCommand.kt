package io.github.nickacpt.event.core.commands

import net.kyori.adventure.text.Component
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

object LocRawCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        // Mods might ask where the player is. This is an hypixel command, but adding just in case.
        // We tell the mod the player is in the limbo.
        sender.sendMessage(Component.text("{\"server\":\"limbo\"}"))
        return true
    }
}