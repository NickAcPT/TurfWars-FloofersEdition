package io.github.nickacpt.replay.platform

import io.github.nickacpt.behaviours.replay.ReplaySystem
import io.github.nickacpt.behaviours.replay.abstractions.EntityManager
import io.github.nickacpt.behaviours.replay.abstractions.ReplayPlatform
import io.github.nickacpt.behaviours.replay.model.RecordedReplayEntity
import io.github.nickacpt.behaviours.replay.model.standard.location.RecordableLocation
import io.github.nickacpt.behaviours.replay.playback.session.ReplaySession
import io.github.nickacpt.replay.platform.abstractions.BukkitReplayViewer
import io.github.nickacpt.replay.platform.abstractions.BukkitReplayWorld
import io.github.nickacpt.replay.platform.abstractions.entity.BukkitReplayEntity
import io.github.nickacpt.replay.platform.abstractions.entity.PlayerEntityData
import io.github.nickacpt.replay.platform.utils.PlayerNameSplittingHelper
import net.citizensnpcs.api.CitizensAPI
import net.citizensnpcs.api.event.SpawnReason
import net.citizensnpcs.api.npc.MemoryNPCDataStore
import net.citizensnpcs.trait.SkinTrait
import net.citizensnpcs.util.NMS
import net.citizensnpcs.util.Util
import net.kyori.adventure.text.Component
import org.bukkit.entity.EntityType

class BukkitEntityManager<
        Platform : ReplayPlatform<BukkitReplayWorld, BukkitReplayViewer, BukkitReplayEntity>,
        System : ReplaySystem<BukkitReplayWorld, BukkitReplayViewer, BukkitReplayEntity, Platform>,
        Session : ReplaySession<BukkitReplayWorld, BukkitReplayViewer, BukkitReplayEntity, Platform, System>,
        >(private val session: Session) : EntityManager<BukkitReplayEntity> {

    private val npcRegistry = CitizensAPI.createAnonymousNPCRegistry(MemoryNPCDataStore())

    override fun spawnEntity(entity: RecordedReplayEntity, location: RecordableLocation): BukkitReplayEntity {
        val world = session.world.bukkitWorld ?: throw IllegalStateException("World is not loaded")
        val data = entity.data

        if (data is PlayerEntityData) {
            val name = PlayerNameSplittingHelper.splitName(data.profile.name)

            val npc = npcRegistry.createNPC(EntityType.PLAYER, name.name)

            npc.getOrAddTrait(SkinTrait::class.java).apply {
                setTexture(data.profile.textures, data.profile.signature)
            }

            npc.data().set("removefromtablist", false)
            npc.spawn(BukkitReplayPlatform.convertIntoPlatformLocation(location, world), SpawnReason.PLUGIN)

            // TODO: Move this out of packets
            val sb = Util.getDummyScoreboard()
            val team = sb.getTeam("npc_${name.name}") ?: sb.registerNewTeam("npc_${name.name}").apply {
                addEntry(name.name)
            }

            team.suffix(Component.text(name.rest))

            session.world.bukkitWorld?.players?.forEach { player ->
                if (player.hasMetadata("NPC")) return@forEach
                NMS.sendTeamPacket(player, team, 0)
            }

            return BukkitReplayEntity(npc.entity)
        } else {
            TODO("Not yet implemented")
        }
    }

    override fun removeEntity(entity: BukkitReplayEntity) {
        entity.bukkitEntity?.remove()
    }

    override fun updateEntityPosition(entity: BukkitReplayEntity, location: RecordableLocation) {
        val bukkitEntity = entity.bukkitEntity ?: return

        BukkitReplayPlatform.applyRecordableLocation(location, bukkitEntity.location)
    }
}
