package io.github.nickacpt.replay.commands

import cloud.commandframework.annotations.Argument
import cloud.commandframework.annotations.CommandMethod
import io.github.nickacpt.behaviours.replay.ReplaySystem
import io.github.nickacpt.behaviours.replay.model.Replay
import io.github.nickacpt.behaviours.replay.playback.session.ReplaySessionState
import io.github.nickacpt.behaviours.replay.record.ReplayRecorder
import io.github.nickacpt.replay.ReplayPlugin
import io.github.nickacpt.replay.platform.BukkitReplayPlatform
import io.github.nickacpt.replay.platform.abstractions.BukkitReplayViewer
import io.github.nickacpt.replay.platform.abstractions.BukkitReplayWorld
import io.github.nickacpt.replay.platform.abstractions.entity.BukkitReplayEntity
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.Player
import java.util.*

object TestCommands {

    private var replay: Replay? = null
    private var recorder: ReplayRecorder<BukkitReplayWorld, BukkitReplayViewer, BukkitReplayEntity, BukkitReplayPlatform, ReplaySystem<BukkitReplayWorld, BukkitReplayViewer, BukkitReplayEntity, BukkitReplayPlatform>>? =
        null

    @CommandMethod("startrecording")
    fun record(player: Player) {
        recorder = ReplayPlugin.replaySystem.createReplayRecorder(
            BukkitReplayPlatform.convertIntoReplayWorld(player.world),
            listOf(BukkitReplayPlatform.convertIntoReplayEntity(player)),
        )
        BukkitReplayPlatform.convertIntoReplayWorld(player.world).recorder = recorder
        replay = recorder!!.replay

        player.sendMessage(Component.text("Started test recording", NamedTextColor.GREEN))
    }

    @CommandMethod("stoprecording")
    fun stop(player: Player) {
        recorder?.stopRecording()
        player.sendMessage(Component.text("Stopped test recording", NamedTextColor.RED))
    }

    @CommandMethod("test")
    fun test(player: Player) {
        if (recorder?.recording == true) {
            stop(player)
        } else {
            if (replay == null) {
                record(player)
            }
            return
        }

        player.sendMessage(Component.text("Playing test recording", NamedTextColor.GOLD))

        ReplayPlugin.replaySystem.createReplaySession(
            replay!!,
            listOf(BukkitReplayPlatform.convertIntoReplayViewer(player))
        )
    }

    @CommandMethod("replaystate <state>")
    fun state(player: Player, @Argument("state") state: ReplaySessionState) {
        val session = BukkitReplayPlatform.convertIntoReplayViewer(player).world?.session ?: return

        session.state = state

        player.sendMessage(Component.text("Set state to $state", NamedTextColor.GOLD))
    }

    @CommandMethod("replaytick <tick>")
    fun tick(player: Player, @Argument("tick") tick: String) {
        val tickULong = tick.toULongOrNull() ?: return
        val session = BukkitReplayPlatform.convertIntoReplayViewer(player).world?.session ?: return

        session.currentTick = tickULong

        player.sendMessage(Component.text("Set tick to $tickULong", NamedTextColor.GOLD))
    }

    @CommandMethod("replayspeed <speed>")
    fun speed(player: Player, @Argument("speed") speed: Double) {
        val session = BukkitReplayPlatform.convertIntoReplayViewer(player).world?.session ?: return

        session.settings.currentPlaybackSpeed = speed

        player.sendMessage(Component.text("Set speed to $speed", NamedTextColor.GOLD))
    }

}