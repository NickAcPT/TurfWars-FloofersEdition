package io.github.nickacpt.replay.platform.utils

import io.github.nickacpt.replay.ReplayPlugin
import io.papermc.paper.adventure.PaperAdventure
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket
import net.minecraft.world.scores.PlayerTeam
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.v1_19_R2.entity.CraftPlayer
import org.bukkit.craftbukkit.v1_19_R2.scoreboard.CraftScoreboard
import org.bukkit.entity.Player

object NmsUtils {
    private var bukkitScoreboard = Bukkit.getScoreboardManager().newScoreboard

    private val vanillaScoreboard get() = (bukkitScoreboard as CraftScoreboard).handle

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
        val playerTeam = PlayerTeam(vanillaScoreboard, teamName)
        playerTeam.playerPrefix = PaperAdventure.asVanilla(prefix)
        playerTeam.playerSuffix = PaperAdventure.asVanilla(suffix)
        playerTeam.color = PaperAdventure.asVanilla(color ?: NamedTextColor.WHITE)!!
        playerTeam.players.addAll(entries)

        sendPacket(
            receiver, ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(
                playerTeam,
                true
            ),
        )

    }

    private fun sendPacket(receiver: Player, vararg packets: Packet<*>) {
        packets.forEach { packet ->
            Bukkit.getScheduler().runTaskLater(
                ReplayPlugin.instance, Runnable {
                    (receiver as CraftPlayer).handle.connection.send(packet)
                }, 10L
            )
        }
    }
}