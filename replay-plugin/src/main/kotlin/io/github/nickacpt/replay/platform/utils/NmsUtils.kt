package io.github.nickacpt.replay.platform.utils

import com.destroystokyo.paper.profile.CraftPlayerProfile
import io.github.nickacpt.behaviours.replay.model.standard.location.HasLook
import io.github.nickacpt.behaviours.replay.model.standard.location.RecordableLocation
import io.github.nickacpt.replay.ReplayPlugin
import io.github.nickacpt.replay.platform.abstractions.entity.BukkitReplayEntity
import io.github.nickacpt.replay.platform.abstractions.entity.PlayerEntityData
import io.papermc.paper.adventure.PaperAdventure
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.scores.PlayerTeam
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.craftbukkit.v1_19_R2.CraftWorld
import org.bukkit.craftbukkit.v1_19_R2.entity.CraftPlayer
import org.bukkit.craftbukkit.v1_19_R2.scoreboard.CraftScoreboard
import org.bukkit.entity.Player

object NmsUtils {
    private var bukkitScoreboard = Bukkit.getScoreboardManager().newScoreboard

    private val vanillaScoreboard get() = (bukkitScoreboard as CraftScoreboard).handle

    fun createFakePlayer(
        world: World,
        data: PlayerEntityData,
        location: RecordableLocation,
    ): BukkitReplayEntity.FakePlayer {
        val gameProfile = data.profile

        val splitName = PlayerNameSplittingHelper.splitName(gameProfile.name)
        val finalProfile = gameProfile.copy(name = splitName.name)

        val player = ServerPlayer(
            MinecraftServer.getServer(),
            (world as CraftWorld).handle,
            (finalProfile.toBukkitProfile() as CraftPlayerProfile).gameProfile
        )

        player.isOnGround = true
        player.isNoGravity = true

        player.setPos(location.x, location.y, location.z)
        if (location is HasLook) {
            player.yRot = location.yaw
            player.xRot = location.pitch
        }

        return BukkitReplayEntity.FakePlayer(player, splitName, player.id, location, data)
    }

    fun sendTeamPacket(
        receiver: Player,
        teamName: String,
        prefix: Component?,
        suffix: Component?,
        color: NamedTextColor?,
        entries: List<String>
    ) {
        println("Sending team packet ($teamName) to $receiver")
        println("Players: $entries")

        if (receiver.scoreboard == Bukkit.getScoreboardManager().mainScoreboard) {
            receiver.scoreboard = bukkitScoreboard
        }


        val packet = createTeamPacket(teamName, prefix, suffix, color, entries)
        for (i in 1..15) {
            sendPacket(
                receiver, i * 10L,
                packet,
            )
        }

    }

    fun createTeamPacket(
        teamName: String,
        prefix: Component?,
        suffix: Component?,
        color: NamedTextColor?,
        entries: List<String>
    ): ClientboundSetPlayerTeamPacket {
        val playerTeam = PlayerTeam(vanillaScoreboard, teamName)
        playerTeam.playerPrefix = PaperAdventure.asVanilla(prefix)
        playerTeam.playerSuffix = PaperAdventure.asVanilla(suffix)
        playerTeam.color = PaperAdventure.asVanilla(color ?: NamedTextColor.WHITE)!!
        playerTeam.players.addAll(entries)

        return ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(
            playerTeam,
            true
        )
    }

    private fun sendPacket(receiver: Player, delay: Long = 0L, packets: Collection<Packet<*>>) {
        packets.forEach { packet ->
            Bukkit.getScheduler().runTaskLater(
                ReplayPlugin.instance, Runnable {
                    (receiver as CraftPlayer).handle.connection.send(packet)
                }, delay + 0L
            )
        }
    }

    internal fun sendPacket(receiver: Player, delay: Long = 0L, vararg packets: Packet<*>) = sendPacket(receiver, delay, packets.toList())
}