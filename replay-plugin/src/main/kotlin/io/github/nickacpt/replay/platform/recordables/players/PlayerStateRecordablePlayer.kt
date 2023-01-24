package io.github.nickacpt.replay.platform.recordables.players

import io.github.nickacpt.behaviours.replay.ReplaySystem
import io.github.nickacpt.behaviours.replay.playback.recordables.EntityRecordablePlayer
import io.github.nickacpt.behaviours.replay.playback.session.ReplaySession
import io.github.nickacpt.replay.platform.BukkitReplayPlatform
import io.github.nickacpt.replay.platform.abstractions.BukkitReplayViewer
import io.github.nickacpt.replay.platform.abstractions.BukkitReplayWorld
import io.github.nickacpt.replay.platform.abstractions.entity.BukkitReplayEntity
import io.github.nickacpt.replay.platform.recordables.PlayerStateRecordable

class PlayerStateRecordablePlayer<System : ReplaySystem<BukkitReplayWorld, BukkitReplayViewer,
        BukkitReplayEntity, BukkitReplayPlatform>,
        Session : ReplaySession<BukkitReplayWorld, BukkitReplayViewer, BukkitReplayEntity, BukkitReplayPlatform, System>
        > : EntityRecordablePlayer<BukkitReplayWorld, BukkitReplayViewer,
        BukkitReplayEntity, BukkitReplayPlatform, System, Session, PlayerStateRecordable> {

    override fun Session.play(tick: ULong, recordable: PlayerStateRecordable, entity: BukkitReplayEntity) {
        if (entity !is BukkitReplayEntity.FakePlayer) return

        when (recordable) {
            is PlayerStateRecordable.Animation -> entity.swingHand(recordable.hand)
            is PlayerStateRecordable.PoseState -> entity.setPose(recordable.pose)
            is PlayerStateRecordable.SkinParts -> entity.setSkinParts(recordable.skinParts, true)
            is PlayerStateRecordable.SneakState -> entity.setIsSneaking(recordable.isSneaking)
            is PlayerStateRecordable.SprintState -> entity.setIsSprinting(recordable.isSprinting)
        }

    }
}