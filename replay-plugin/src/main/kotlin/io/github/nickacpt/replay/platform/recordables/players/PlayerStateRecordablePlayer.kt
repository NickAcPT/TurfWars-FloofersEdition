package io.github.nickacpt.replay.platform.recordables.players

import io.github.nickacpt.behaviours.replay.ReplaySystem
import io.github.nickacpt.behaviours.replay.playback.recordables.EntityRecordablePlayer
import io.github.nickacpt.behaviours.replay.playback.session.ReplaySession
import io.github.nickacpt.replay.platform.BukkitReplayPlatform
import io.github.nickacpt.replay.platform.abstractions.BukkitReplayViewer
import io.github.nickacpt.replay.platform.abstractions.BukkitReplayWorld
import io.github.nickacpt.replay.platform.abstractions.entity.BukkitReplayEntity
import io.github.nickacpt.replay.platform.recordables.PlayerStateRecordable
import net.citizensnpcs.util.PlayerAnimation
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerAnimationType

class PlayerStateRecordablePlayer<System : ReplaySystem<BukkitReplayWorld, BukkitReplayViewer,
        BukkitReplayEntity, BukkitReplayPlatform>,
        Session : ReplaySession<BukkitReplayWorld, BukkitReplayViewer, BukkitReplayEntity, BukkitReplayPlatform, System>
        > : EntityRecordablePlayer<BukkitReplayWorld, BukkitReplayViewer,
        BukkitReplayEntity, BukkitReplayPlatform, System, Session, PlayerStateRecordable> {

    override fun Session.play(tick: ULong, recordable: PlayerStateRecordable, entity: BukkitReplayEntity) {
        val bukkitEntity = entity.bukkitEntity as? Player ?: return

        when (recordable) {
            is PlayerStateRecordable.SprintState -> {
                bukkitEntity.isSprinting = recordable.isSprinting
            }

            is PlayerStateRecordable.Animation -> {
                val animation =
                    if (recordable.hand == PlayerAnimationType.ARM_SWING) PlayerAnimation.ARM_SWING else PlayerAnimation.ARM_SWING_OFFHAND
                animation.play(bukkitEntity)
            }

            is PlayerStateRecordable.SneakState -> {
                val animation = if (recordable.isSneaking) PlayerAnimation.SNEAK else PlayerAnimation.STOP_SNEAKING
                animation.play(bukkitEntity)
            }
        }
    }
}