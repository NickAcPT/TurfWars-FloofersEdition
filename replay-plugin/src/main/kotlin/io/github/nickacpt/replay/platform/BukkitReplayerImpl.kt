package io.github.nickacpt.replay.platform

import io.github.nickacpt.behaviours.replay.ReplaySystem
import io.github.nickacpt.behaviours.replay.abstractions.EntityManager
import io.github.nickacpt.behaviours.replay.abstractions.ReplayPlatform
import io.github.nickacpt.behaviours.replay.logic.ReplayControlItemType
import io.github.nickacpt.behaviours.replay.model.Replay
import io.github.nickacpt.behaviours.replay.playback.Replayer
import io.github.nickacpt.behaviours.replay.playback.session.ReplaySession
import io.github.nickacpt.behaviours.replay.playback.session.ReplaySessionState
import io.github.nickacpt.behaviours.replay.utils.displayTicks
import io.github.nickacpt.replay.platform.abstractions.BukkitReplayViewer
import io.github.nickacpt.replay.platform.abstractions.BukkitReplayWorld
import io.github.nickacpt.replay.platform.abstractions.entity.BukkitReplayEntity
import io.github.nickacpt.replay.platform.utils.NmsUtils
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.JoinConfiguration
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.util.TriState
import org.bukkit.*
import java.util.*

class BukkitReplayerImpl :
    Replayer<BukkitReplayWorld, BukkitReplayViewer, BukkitReplayEntity, BukkitReplayPlatform,
            ReplaySystem<BukkitReplayWorld, BukkitReplayViewer, BukkitReplayEntity, BukkitReplayPlatform>> {

    companion object {
        const val REPLAY_WORLD_NAME_PRFIX = "replay_world_"
    }

    override fun prepareReplaySession(
        replay: Replay,
        replaySession: ReplaySession<BukkitReplayWorld, BukkitReplayViewer, BukkitReplayEntity, BukkitReplayPlatform,
                ReplaySystem<BukkitReplayWorld, BukkitReplayViewer, BukkitReplayEntity, BukkitReplayPlatform>>,
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
        for (x in -5..5) {
            for (z in -5..5) {
                world.setBlockData(x, 74, z, Bukkit.createBlockData(Material.BEDROCK))
            }
        }

        replayViewers.forEach {
            setupReplayViewer(it, world, replayViewers)
        }

        return BukkitReplayPlatform.convertIntoReplayWorld(world).also {
            it.session = replaySession
        }
    }

    override fun <Platform : ReplayPlatform<BukkitReplayWorld, BukkitReplayViewer, BukkitReplayEntity>, System : ReplaySystem<BukkitReplayWorld, BukkitReplayViewer, BukkitReplayEntity, Platform>, Session : ReplaySession<BukkitReplayWorld, BukkitReplayViewer, BukkitReplayEntity, Platform, System>> createEntityManager(
        replaySystem: System,
        replaySession: Session
    ): EntityManager<BukkitReplayEntity> {
        return BukkitEntityManager(replaySession)
    }

    private fun setupReplayViewer(viewer: BukkitReplayViewer, world: World, viewers: List<BukkitReplayViewer>) {
        val player = viewer.bukkitPlayer ?: return

        player.teleport(Location(world, 0.0, 75.0, 0.0))

        viewers.forEach {
            val otherPlayer = it.bukkitPlayer ?: return@forEach

            NmsUtils.sendTeamPacket(
                otherPlayer,
                "v_${player.uniqueId.hashCode()}",
                Component.text("[Viewer] ", NamedTextColor.GRAY),
                null,
                NamedTextColor.GRAY,
                listOf(player.name)
            )
        }

        run {
            player.gameMode = GameMode.ADVENTURE
            player.allowFlight = true
            player.isFlying = true
        }
    }

    override fun updateReplaySessionStateForViewer(
        replaySession: ReplaySession<BukkitReplayWorld, BukkitReplayViewer, BukkitReplayEntity, BukkitReplayPlatform, ReplaySystem<BukkitReplayWorld, BukkitReplayViewer, BukkitReplayEntity, BukkitReplayPlatform>>,
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
            .append(Component.text(" / ", NamedTextColor.YELLOW))
            .append(displayTicks(replaySession.replay.duration).color(NamedTextColor.YELLOW))

        val speed =
            Component.text("${"%.1f".format(replaySession.settings.currentPlaybackSpeed)}x", NamedTextColor.GOLD)

        val message = Component.join(JoinConfiguration.separator(space), state, time, speed)

        viewer.sendActionBar(message)

        viewer.bukkitPlayer?.sendExperienceChange(replaySession.currentTick.toFloat() / replaySession.replay.duration.toFloat())
    }

    override fun updateReplaySessionViewerControls(
        replaySession: ReplaySession<BukkitReplayWorld, BukkitReplayViewer, BukkitReplayEntity, BukkitReplayPlatform, ReplaySystem<BukkitReplayWorld, BukkitReplayViewer, BukkitReplayEntity, BukkitReplayPlatform>>,
        viewer: BukkitReplayViewer
    ) {
        val stateItem = when (replaySession.state) {
            ReplaySessionState.PAUSED -> ReplayControlItemType.RESUME
            ReplaySessionState.FINISHED -> ReplayControlItemType.RESTART
            else -> ReplayControlItemType.PAUSE
        }

        val items = mapOf(
            2 to ReplayControlItemType.DECREASE_SPEED,
            3 to ReplayControlItemType.STEP_BACKWARDS,
            4 to stateItem,
            5 to ReplayControlItemType.STEP_FORWARD,
            6 to ReplayControlItemType.INCREASE_SPEED
        )

        val player = viewer.bukkitPlayer ?: return

        items.forEach { item ->
            player.inventory.setItem(item.key, ReplayItemStackUtils.createControlItemStack(item.value, replaySession))
        }
    }
}