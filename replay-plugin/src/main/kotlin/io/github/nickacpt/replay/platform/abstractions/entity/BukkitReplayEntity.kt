package io.github.nickacpt.replay.platform.abstractions.entity

import com.destroystokyo.paper.ClientOption
import io.github.nickacpt.behaviours.replay.abstractions.ReplayEntity
import io.github.nickacpt.behaviours.replay.abstractions.ReplayEntityData
import io.github.nickacpt.behaviours.replay.model.standard.location.HasLook
import io.github.nickacpt.behaviours.replay.model.standard.location.RecordableLocation
import io.github.nickacpt.replay.platform.BukkitReplayPlatform
import io.github.nickacpt.replay.platform.utils.NmsUtils
import io.github.nickacpt.replay.platform.utils.PlayerNameSplitData
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.*
import net.minecraft.server.level.ServerPlayer
import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerAnimationType
import java.util.*
import kotlin.math.roundToInt
import net.minecraft.world.entity.Pose as NmsPose
import net.minecraft.world.entity.player.Player as NmsPlayer
import org.bukkit.entity.Pose as BukkitPose

sealed class BukkitReplayEntity : ReplayEntity {
    data class FakePlayer(
        internal val handle: ServerPlayer,
        val splitName: PlayerNameSplitData,
        override val id: Int,
        override var location: RecordableLocation,
        override var data: PlayerEntityData
    ) : BukkitReplayEntity() {
        init {
            setSkinParts(data.skinParts, false)
        }

        private val viewers = mutableSetOf<UUID>()

        private fun getCreationPackets(): List<Packet<*>> {
            val addEntityToTabList = ClientboundPlayerInfoUpdatePacket.createPlayerInitializing(listOf(handle))
            val addEntity = ClientboundAddPlayerPacket(handle)
            val teamPacket = NmsUtils.createTeamPacket(
                "npc_${id}",
                null,
                Component.text(splitName.rest),
                NamedTextColor.RED,
                listOf(splitName.name)
            )

            return listOf(
                addEntityToTabList,
                addEntity,
                teamPacket
            )
        }

        fun spawn(player: Player) {
            viewers.add(player.uniqueId)
            NmsUtils.sendPacket(player, 0, *getCreationPackets().toTypedArray())
        }

        private fun encode(value: Double): Short {
            return (value * 4096.0).roundToInt().toShort()
        }

        private fun sendEntityDataUpdatePacket() {
            getEntityDataUpdatePacket()?.let { sendPacketToViewers(0, it) }
        }

        fun setPose(pose: BukkitPose) {
            handle.pose = NmsPose.values()[pose.ordinal]
            sendEntityDataUpdatePacket()
        }

        fun setIsSprinting(isSprinting: Boolean) {
            handle.isSprinting = isSprinting
            sendEntityDataUpdatePacket()
        }

        fun setIsSneaking(isSneaking: Boolean) {
            handle.isShiftKeyDown = isSneaking
            sendEntityDataUpdatePacket()
        }

        fun teleport(location: RecordableLocation) {
            val deltaX = encode(location.x - this.location.x)
            val deltaY = encode(location.y - this.location.y)
            val deltaZ = encode(location.z - this.location.z)
            this.location = location

            val pitch = (((location as? HasLook)?.pitch ?: 0f) * 256.0F / 360.0F).toInt().toByte()
            val yaw = (((location as? HasLook)?.yaw ?: 0f) * 256.0F / 360.0F).toInt().toByte()

            val teleportPacket =
                ClientboundMoveEntityPacket.PosRot(handle.id, deltaX, deltaY, deltaZ, yaw, pitch, handle.isOnGround)
            val lookPacket = ClientboundRotateHeadPacket(handle, yaw)

            sendPacketToViewers(0, teleportPacket, lookPacket)
        }

        private fun getEntityDataUpdatePacket(): Packet<*>? {
            return handle.entityData.packDirty()?.let { ClientboundSetEntityDataPacket(handle.id, it) }
        }

        fun setSkinParts(skinParts: Int, send: Boolean) {
            handle.entityData.set(NmsPlayer.DATA_PLAYER_MODE_CUSTOMISATION, skinParts.toByte())
            if (send) sendEntityDataUpdatePacket()
        }

        fun swingHand(hand: PlayerAnimationType) {
            sendPacketToViewers(
                0, ClientboundAnimatePacket(
                    handle,
                    if (hand == PlayerAnimationType.ARM_SWING) ClientboundAnimatePacket.SWING_MAIN_HAND else ClientboundAnimatePacket.SWING_OFF_HAND
                )
            )
        }

        private fun sendPacketToViewers(delay: Long = 0, vararg packets: Packet<*>) {
            viewers.forEach {
                val player = Bukkit.getPlayer(it)
                if (player != null) {
                    NmsUtils.sendPacket(player, delay, *packets)
                }
            }
        }
    }

    class BukkitEntity(entity: Entity) : BukkitReplayEntity() {
        private val entityUuid = entity.uniqueId
        internal val bukkitEntity = Bukkit.getEntity(entityUuid)

        override val id: Int = entity.entityId

        override val location: RecordableLocation
            get() = BukkitReplayPlatform.convertIntoReplayLocation(bukkitEntity?.location)

        override val data: ReplayEntityData
            get() = when (bukkitEntity) {
                is Player -> PlayerEntityData(
                    PlayerGameProfile.fromBukkitProfile(bukkitEntity),
                    bukkitEntity.getClientOption(ClientOption.SKIN_PARTS).raw
                )

                null -> EmptyEntityData
                else -> SerializedEntityData(Bukkit.getUnsafe().serializeEntity(bukkitEntity))
            }
    }
}