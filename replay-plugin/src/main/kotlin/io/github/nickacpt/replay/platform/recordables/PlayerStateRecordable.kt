package io.github.nickacpt.replay.platform.recordables

import io.github.nickacpt.behaviours.replay.model.Recordable
import io.github.nickacpt.behaviours.replay.model.RecordedReplayEntity
import io.github.nickacpt.behaviours.replay.model.standard.entity.HasEntity
import org.bukkit.event.player.PlayerAnimationType

sealed class PlayerStateRecordable : Recordable, HasEntity {
    data class SprintState(val isSprinting: Boolean, override var entity: RecordedReplayEntity?) : PlayerStateRecordable()
    data class Animation(val hand: PlayerAnimationType, override var entity: RecordedReplayEntity?) : PlayerStateRecordable()
    data class SneakState(val isSneaking: Boolean, override var entity: RecordedReplayEntity?) : PlayerStateRecordable()
}
