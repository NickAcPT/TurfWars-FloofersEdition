package io.github.nickacpt.event.utils

import cloud.commandframework.Command
import cloud.commandframework.annotations.AnnotationParser
import cloud.commandframework.execution.CommandExecutionCoordinator
import cloud.commandframework.paper.PaperCommandManager
import io.github.nickacpt.event.core.players.TurfPlayer
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

class TurfCommandManager(plugin: Plugin) : PaperCommandManager<TurfPlayer>(
    plugin,
    CommandExecutionCoordinator.simpleCoordinator(),
    { if (it is Player) it.turfPlayer else null },
    { sender -> sender.bukkitPlayer }
) {
    init {
        registerAsynchronousCompletions()
    }

    val annotationParser = AnnotationParser(this, TurfPlayer::class.java) { createDefaultCommandMeta() }

    fun parseCommands(vararg commands: Any): List<Command<TurfPlayer>> {
        return commands.flatMap{ annotationParser.parse(it) }
    }
}