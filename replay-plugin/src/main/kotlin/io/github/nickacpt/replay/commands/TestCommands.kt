package io.github.nickacpt.replay.commands

import cloud.commandframework.annotations.CommandMethod
import io.github.nickacpt.behaviours.replay.model.Replay
import io.github.nickacpt.replay.ReplayPlugin
import org.bukkit.entity.Player
import java.util.*

object TestCommands {

    @CommandMethod("test")
    fun test(player: Player) {
        val replay = Replay(UUID.randomUUID(), emptyMap(), listOf())
        ReplayPlugin.replaySystem.createReplaySession(replay, listOf(player))
    }

}