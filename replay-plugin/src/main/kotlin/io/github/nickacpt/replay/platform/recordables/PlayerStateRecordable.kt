package io.github.nickacpt.replay.platform.recordables

import io.github.nickacpt.behaviours.replay.model.Recordable
import io.github.nickacpt.behaviours.replay.model.RecordedReplayEntity
import io.github.nickacpt.behaviours.replay.model.standard.DefaultRecordableProvider
import io.github.nickacpt.behaviours.replay.model.standard.entity.HasEntity
import org.bukkit.entity.Pose
import org.bukkit.event.player.PlayerAnimationType

sealed class PlayerStateRecordable : Recordable, HasEntity {
    data class Animation(val hand: PlayerAnimationType, override var entity: RecordedReplayEntity?) :
        PlayerStateRecordable()

    data class PoseState(val pose: Pose, override var entity: RecordedReplayEntity?) : PlayerStateRecordable() {
        companion object : DefaultRecordableProvider<PoseState> {
            override fun getDefault(): PoseState = PoseState(Pose.STANDING, null)
        }
    }
    data class SkinParts(val skinParts: Int, override var entity: RecordedReplayEntity?) : PlayerStateRecordable()
    data class SneakState(val isSneaking: Boolean, override var entity: RecordedReplayEntity?) :
        PlayerStateRecordable() {
        companion object : DefaultRecordableProvider<SneakState> {
            override fun getDefault(): SneakState = SneakState(false, null)
        }
    }

    data class SprintState(val isSprinting: Boolean, override var entity: RecordedReplayEntity?) :
        PlayerStateRecordable() {
        companion object : DefaultRecordableProvider<SprintState> {
            override fun getDefault(): SprintState = SprintState(false, null)
        }
    }

}
