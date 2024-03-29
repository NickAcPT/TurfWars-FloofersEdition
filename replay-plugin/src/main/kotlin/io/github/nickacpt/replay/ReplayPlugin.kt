package io.github.nickacpt.replay

import cloud.commandframework.annotations.AnnotationParser
import cloud.commandframework.execution.CommandExecutionCoordinator
import cloud.commandframework.paper.PaperCommandManager
import io.github.nickacpt.behaviours.replay.ReplaySystem
import io.github.nickacpt.replay.commands.TestCommands
import io.github.nickacpt.replay.event.ReplayItemsListener
import io.github.nickacpt.replay.event.ReplayWorldEventListener
import io.github.nickacpt.replay.platform.BukkitReplayPlatform
import io.github.nickacpt.replay.platform.recordables.PlayerStateRecordable
import io.github.nickacpt.replay.platform.recordables.players.PlayerStateRecordablePlayer
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import java.util.function.Function

class ReplayPlugin : JavaPlugin() {
    companion object {
        val replaySystem = ReplaySystem(BukkitReplayPlatform)

        val instance get() = getPlugin(ReplayPlugin::class.java)
    }

    private val commandManager by lazy {
        PaperCommandManager(
            this,
            CommandExecutionCoordinator.simpleCoordinator(),
            Function.identity(),
            Function.identity()
        )
    }

    private val annotationParser by lazy {
        AnnotationParser(commandManager, CommandSender::class.java) { commandManager.createDefaultCommandMeta() }
    }

    override fun onEnable() {
        Bukkit.getPluginManager().registerEvents(ReplayWorldEventListener, this)
        Bukkit.getPluginManager().registerEvents(ReplayItemsListener, this)

        annotationParser.parse(TestCommands)

        replaySystem.apply {
            registerRecordablePlayer(PlayerStateRecordable.Animation::class.java, PlayerStateRecordablePlayer())
            registerRecordablePlayer(PlayerStateRecordable.PoseState::class.java, PlayerStateRecordablePlayer())
            registerRecordablePlayer(PlayerStateRecordable.SkinParts::class.java, PlayerStateRecordablePlayer())
            registerRecordablePlayer(PlayerStateRecordable.SneakState::class.java, PlayerStateRecordablePlayer())
            registerRecordablePlayer(PlayerStateRecordable.SprintState::class.java, PlayerStateRecordablePlayer())

            registerRecordableDefaultProvider(PlayerStateRecordable.PoseState)
            registerRecordableDefaultProvider(PlayerStateRecordable.SneakState)
            registerRecordableDefaultProvider(PlayerStateRecordable.SprintState)

            initialize()
        }
    }
}