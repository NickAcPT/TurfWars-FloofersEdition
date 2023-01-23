package io.github.nickacpt.replay.platform

import io.github.nickacpt.behaviours.replay.ReplaySystem
import io.github.nickacpt.behaviours.replay.abstractions.EntityManager
import io.github.nickacpt.behaviours.replay.abstractions.ReplayPlatform
import io.github.nickacpt.behaviours.replay.logic.ReplayControlItemType
import io.github.nickacpt.behaviours.replay.model.Replay
import io.github.nickacpt.behaviours.replay.playback.Replayer
import io.github.nickacpt.behaviours.replay.playback.session.ReplaySession
import io.github.nickacpt.behaviours.replay.playback.session.ReplaySessionState
import io.github.nickacpt.replay.platform.abstractions.BukkitReplayViewer
import io.github.nickacpt.replay.platform.abstractions.BukkitReplayWorld
import io.github.nickacpt.replay.platform.abstractions.entity.BukkitReplayEntity
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.JoinConfiguration
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.util.TriState
import org.bukkit.*
import java.util.*

class BukkitReplayerImpl :
    Replayer<BukkitReplayViewer, BukkitReplayWorld, BukkitReplayEntity, BukkitReplayPlatform,
            ReplaySystem<BukkitReplayViewer, BukkitReplayWorld, BukkitReplayEntity, BukkitReplayPlatform>> {

    companion object {
        const val REPLAY_WORLD_NAME_PRFIX = "replay_world_"

        private const val TICKS_PER_SECOND = 20
        private const val SECONDS_PER_MINUTE = 60
        private const val MINUTES_PER_HOUR = 60
    }

    override fun prepareReplaySession(
        replay: Replay,
        replaySession: ReplaySession<BukkitReplayViewer, BukkitReplayWorld, BukkitReplayEntity, BukkitReplayPlatform,
                ReplaySystem<BukkitReplayViewer, BukkitReplayWorld, BukkitReplayEntity, BukkitReplayPlatform>>,
        replayViewers: List<BukkitReplayViewer>
    ): BukkitReplayWorld {
        val world = WorldCreator(REPLAY_WORLD_NAME_PRFIX + UUID.randomUUID().toString().replace("-", ""))
            .type(WorldType.FLAT)
            .generatorSettings("{\"layers\":[{\"block\":\"air\",\"height\":1}],\"biome\":\"plains\"}")
            .keepSpawnLoaded(TriState.FALSE)
            .createWorld()

        if (world == null) {
            replaySession.sendMessage(
                Component.text(
                    "Unable to create a world to host the replay session.",
                    NamedTextColor.RED
                )
            )

            throw IllegalStateException("Unable to create a world to host the replay session.")
        }

        // TODO: Load chunks
        world.setBlockData(0, 99, 0, Bukkit.createBlockData(Material.BEDROCK))

        replayViewers.forEach {
            setupReplayViewer(it, world)
        }

        return BukkitReplayPlatform.convertIntoReplayWorld(world)
    }

    override fun <Platform : ReplayPlatform<BukkitReplayViewer, BukkitReplayWorld, BukkitReplayEntity>, System : ReplaySystem<BukkitReplayViewer, BukkitReplayWorld, BukkitReplayEntity, Platform>, Session : ReplaySession<BukkitReplayViewer, BukkitReplayWorld, BukkitReplayEntity, Platform, System>> createEntityManager(
        replaySystem: System,
        replaySession: Session
    ): EntityManager<BukkitReplayEntity> {
        return BukkitEntityManager(replaySession)
    }

    private fun setupReplayViewer(viewer: BukkitReplayViewer, world: World) {
        val player = viewer.bukkitPlayer ?: return

        player.teleportAsync(Location(world, 0.0, 100.0, 0.0)).thenAccept {
            player.gameMode = GameMode.ADVENTURE
            player.allowFlight = true
            player.isFlying = true
        }
    }

    /**
     * Formats the given number of ticks into hours, minutes and seconds.
     *
     * @param ticks The number of ticks to format
     * @return The formatted time in hh:mm:ss format
     */
    fun displayTicks(ticks: Long): Component {
        val ticksPerMinute = TICKS_PER_SECOND * SECONDS_PER_MINUTE

        // Convert ticks to minutes
        val minutes = ticks / ticksPerMinute
        // Get the remaining seconds by using the modulo operator
        val seconds = (ticks % ticksPerMinute) / TICKS_PER_SECOND

        // Calculate the number of hours from the total number of minutes
        val hours = minutes / MINUTES_PER_HOUR

        // Calculate the remaining minutes after the hours have been extracted
        val finalMinutes = minutes % MINUTES_PER_HOUR

        val timeComponents = buildList {
            if (hours > 0) add(hours)
            add(finalMinutes)
            add(seconds)
        }

        return Component.join(
            JoinConfiguration.separator(Component.text(":")),
            timeComponents.map { Component.text("%02d".format(it)) }
        )
    }

    override fun updateReplaySessionStateForViewer(
        replaySession: ReplaySession<BukkitReplayViewer, BukkitReplayWorld, BukkitReplayEntity, BukkitReplayPlatform, ReplaySystem<BukkitReplayViewer, BukkitReplayWorld, BukkitReplayEntity, BukkitReplayPlatform>>,
        viewer: BukkitReplayViewer
    ) {
        val space = Component.text(" ".repeat(6), NamedTextColor.WHITE)

        val state = when (replaySession.state) {
            ReplaySessionState.LOADING -> Component.text("Loading", NamedTextColor.YELLOW)
            ReplaySessionState.PAUSED -> Component.text("Paused", NamedTextColor.RED)
            ReplaySessionState.PLAYING -> Component.text("Playing", NamedTextColor.GREEN)
            ReplaySessionState.FINISHED -> Component.text("Finished", NamedTextColor.RED)
        }

        val time = displayTicks(replaySession.currentTick).color(NamedTextColor.YELLOW)
            .append(Component.text(" / TOTAL", NamedTextColor.YELLOW))

        val speed =
            Component.text("${"%.1f".format(replaySession.settings.currentPlaybackSpeed)}x", NamedTextColor.GOLD)

        val message = Component.join(JoinConfiguration.separator(space), state, time, speed)

        viewer.sendActionBar(message)
    }

    override fun updateReplaySessionViewerControls(
        replaySession: ReplaySession<BukkitReplayViewer, BukkitReplayWorld, BukkitReplayEntity, BukkitReplayPlatform, ReplaySystem<BukkitReplayViewer, BukkitReplayWorld, BukkitReplayEntity, BukkitReplayPlatform>>,
        viewer: BukkitReplayViewer
    ) {
        val items = mapOf(
            2 to ReplayControlItemType.DECREASE_SPEED,
            3 to ReplayControlItemType.STEP_BACKWARDS,
            4 to ReplayControlItemType.RESUME,
            5 to ReplayControlItemType.STEP_FORWARD,
            6 to ReplayControlItemType.INCREASE_SPEED
        )

        val player = viewer.bukkitPlayer ?: return

        items.forEach { item ->
            player.inventory.setItem(item.key, ReplayItemStackUtils.createControlItemStack(item.value, replaySession))
        }
    }
}