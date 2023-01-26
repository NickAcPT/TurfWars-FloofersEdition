package io.github.nickacpt.event.utils

import cloud.commandframework.annotations.AnnotationParser
import cloud.commandframework.execution.AsynchronousCommandExecutionCoordinator
import cloud.commandframework.paper.PaperCommandManager
import io.github.nickacpt.event.core.players.TurfPlayer
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

class TurfCommandManager(plugin: Plugin) : PaperCommandManager<TurfPlayer>(
    plugin,
    AsynchronousCommandExecutionCoordinator.builder<TurfPlayer>().withAsynchronousParsing().build(),
    { if (it is Player) it.turfPlayer else null },
    { sender -> sender.bukkitPlayer }
) {
    init {
        registerAsynchronousCompletions()
    }

    val annotationParser = AnnotationParser(this, TurfPlayer::class.java) { createDefaultCommandMeta() }
}