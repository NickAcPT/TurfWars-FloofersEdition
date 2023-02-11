package io.github.nickacpt.event.utils

import cloud.commandframework.Command
import cloud.commandframework.annotations.AnnotationParser
import cloud.commandframework.execution.AsynchronousCommandExecutionCoordinator
import cloud.commandframework.kotlin.coroutines.annotations.installCoroutineSupport
import cloud.commandframework.paper.PaperCommandManager
import io.github.nickacpt.event.core.players.TurfPlayer
import io.github.nickacpt.event.utils.coroutines.CoroutineUtils
import io.github.nickacpt.event.utils.coroutines.MinecraftSchedulerDispatchers
import kotlinx.coroutines.asExecutor
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

class TurfCommandManager(plugin: Plugin) : PaperCommandManager<TurfPlayer>(
    plugin,
    AsynchronousCommandExecutionCoordinator.builder<TurfPlayer>()
        .withExecutor(MinecraftSchedulerDispatchers.SYNC.asExecutor())
        .withAsynchronousParsing()
        .build(),
    { if (it is Player) it.turfPlayer else null },
    { sender -> sender.bukkitPlayer }
) {
    init {
        registerAsynchronousCompletions()
    }

    val annotationParser = AnnotationParser(this, TurfPlayer::class.java) { createDefaultCommandMeta() }.apply {
        installCoroutineSupport(CoroutineUtils.scope, MinecraftSchedulerDispatchers.SYNC)
    }

    fun parseCommands(vararg commands: Any): List<Command<TurfPlayer>> {
        return commands.flatMap { annotationParser.parse(it) }
    }
}