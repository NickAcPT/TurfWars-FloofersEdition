package io.github.nickacpt.replay.platform

import com.destroystokyo.paper.event.player.PlayerClientOptionsChangeEvent
import io.github.nickacpt.behaviours.replay.ReplaySystem
import io.github.nickacpt.behaviours.replay.record.ReplayRecorder
import io.github.nickacpt.replay.platform.abstractions.BukkitReplayViewer
import io.github.nickacpt.replay.platform.abstractions.BukkitReplayWorld
import io.github.nickacpt.replay.platform.abstractions.entity.BukkitReplayEntity
import io.github.nickacpt.replay.platform.recordables.PlayerStateRecordable
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPoseChangeEvent
import org.bukkit.event.player.PlayerAnimationEvent
import org.bukkit.event.player.PlayerToggleSneakEvent
import org.bukkit.event.player.PlayerToggleSprintEvent

class BukkitReplayRecorderImpl(
    private val world: BukkitReplayWorld,
    system: ReplaySystem<BukkitReplayWorld, BukkitReplayViewer, BukkitReplayEntity, BukkitReplayPlatform>,
    entities: List<BukkitReplayEntity>
) : Listener,
    ReplayRecorder<BukkitReplayWorld, BukkitReplayViewer, BukkitReplayEntity, BukkitReplayPlatform, ReplaySystem<BukkitReplayWorld, BukkitReplayViewer, BukkitReplayEntity, BukkitReplayPlatform>>(
        system,
        entities
    ) {

    @EventHandler
    fun onPlayerAnimation(e: PlayerAnimationEvent) {
        if (e.player.world.uid != world.id) return

        addEntityRecordable(
            PlayerStateRecordable.Animation(e.animationType, null),
            BukkitReplayPlatform.convertIntoReplayEntity(e.player)
        )
    }

    @EventHandler
    fun onPlayerSprintToggle(e: PlayerToggleSprintEvent) {
        if (e.player.world.uid != world.id) return

        addEntityRecordable(
            PlayerStateRecordable.SprintState(e.isSprinting, null),
            BukkitReplayPlatform.convertIntoReplayEntity(e.player)
        )
    }

    @EventHandler
    fun onPlayerSneakToggle(e: PlayerToggleSneakEvent) {
        if (e.player.world.uid != world.id) return

        addEntityRecordable(
            PlayerStateRecordable.SneakState(e.isSneaking, null),
            BukkitReplayPlatform.convertIntoReplayEntity(e.player)
        )
    }


    @EventHandler
    fun onPoseChange(e: EntityPoseChangeEvent) {
        if (e.entity.world.uid != world.id) return
        if (e.entity !is Player) return

        addEntityRecordable(
            PlayerStateRecordable.PoseState(e.pose, null),
            BukkitReplayPlatform.convertIntoReplayEntity(e.entity)
        )
    }
    @EventHandler
    fun onClientOptionsChange(e: PlayerClientOptionsChangeEvent) {
        if (e.player.world.uid != world.id) return

        if (e.hasSkinPartsChanged()) {
            addEntityRecordable(
                PlayerStateRecordable.SkinParts(e.skinParts.raw, null),
                BukkitReplayPlatform.convertIntoReplayEntity(e.player)
            )
        }
    }

}
