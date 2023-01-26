package io.github.nickacpt.event.core.commands

import cloud.commandframework.annotations.CommandMethod
import io.github.nickacpt.event.core.CorePlugin
import io.github.nickacpt.event.core.players.TurfPlayer

object FakeBukkitCommands {

    @CommandMethod("help")
    fun helpCommand(player: TurfPlayer) {
        CorePlugin.instance.messages.helpMessage(player)
    }
}