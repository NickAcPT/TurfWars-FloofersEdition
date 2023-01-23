package io.github.nickacpt.replay.commands

import cloud.commandframework.annotations.CommandMethod
import io.github.nickacpt.behaviours.replay.model.RecordedReplayEntity
import io.github.nickacpt.behaviours.replay.model.Replay
import io.github.nickacpt.behaviours.replay.model.metadata.def.ReplayRecordingInformation
import io.github.nickacpt.behaviours.replay.model.standard.location.RecordableLocationWithLook
import io.github.nickacpt.replay.ReplayPlugin
import io.github.nickacpt.replay.platform.BukkitReplayPlatform
import io.github.nickacpt.replay.platform.abstractions.entity.PlayerEntityData
import io.github.nickacpt.replay.platform.abstractions.entity.PlayerGameProfile
import org.bukkit.entity.Player
import java.time.Instant
import java.util.*

object TestCommands {

    @CommandMethod("test")
    fun test(player: Player) {
        val entity = RecordedReplayEntity(
            1,
            RecordableLocationWithLook(0.5, 100.0, 0.5, 0.0f, 0.0f),
            PlayerEntityData(
                PlayerGameProfile(
                    "NickAc",
                    "ewogICJ0aW1lc3RhbXAiIDogMTY3NDQwOTE5NzM1NSwKICAicHJvZmlsZUlkIiA6ICJhZDQ1NjlmMzc1NzY0Mzc2YTdj" +
                            "NzhlOGNmY2Q5YjgzMiIsCiAgInByb2ZpbGVOYW1lIiA6ICJOaWNrQWMiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB" +
                            "0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy" +
                            "5taW5lY3JhZnQubmV0L3RleHR1cmUvMjAyZWUzYWJlN2JmYjdhYzM0NjQwY2FlNGQ3Yzc5ZTIxZWU2N2U3ZTJhODg0Y" +
                            "mE3MDJhYTAxOTExZjkxZDIxOCIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgog" +
                            "ICAgICB9CiAgICB9CiAgfQp9",
                    "KifmiIkc/2xpk0EPIQUVKm560gVRSwY9/qn3QvvNOn8UUCtd0w2Uoi32Zmx95zqxxfzrOBXYcFJlSNjYrzrUw5YF6WL" +
                            "+A5kj2AekAILvluAuSqVCcMMe3GSr+KeSGcvYvhkVkCTr/8T/KxcCZJBBvtzlMm77E8lHngHrqBY/hqsXMTkQZ1lEZn" +
                            "iPlLoP/sa5QPR5Dk2R0njORwxNN0zhWwBxgxCGAJK5VyBFAXro4b6C+PR1tt2Qth4bXi/7H3qPBKGnJASK8DkI2Mbfx" +
                            "m3+BdPzbUBHnN6ygnyOK3ciZujP4tDvw6SrTnElDlLZN77lWm1QCt30L8Rpo+wZx1Bei0GKBKIOOQTbgYbCNwdtMdZO" +
                            "omu3t4baUbDgJfLiPMK5EEvxwqx9fDdjdPGdRh1ZCQQOgw5uAfx6Sxlf3fsNNc7R8x+55eK8hIO8u92GzJAhftL2/jj" +
                            "qY3FCKfxzP+l26+tG1NPhbA9KxraqCCeC/cmiSUUOCDNZd82xII+Dfc/6GuLwr9NzLQWMZ0MQEyGWwdortFQdTX0QHw" +
                            "zO9MDDvtUPqSHMvA/WGHu+fPUT0BwyNQDNBdjKnOkqSfwMM2QvKgzU6Osb7KSjTqqSnjLy9o0u+JVH3DEHbh6dDFt6m" +
                            "vjIMDSnpDLNiiQZvlm0av4PyY1IUPFv8+6Ka/pm1UipBXw="
                )
            )
        )

        val replay = Replay(
            UUID.randomUUID(), listOf(entity), mutableMapOf(
                ReplayPlugin.replaySystem.defaultMetadataKeys!!.recordingInformation to ReplayRecordingInformation(
                    Instant.now()
                )
            ), listOf()
        )
        ReplayPlugin.replaySystem.createReplaySession(
            replay,
            listOf(BukkitReplayPlatform.convertIntoReplayViewer(player))
        )
    }

}