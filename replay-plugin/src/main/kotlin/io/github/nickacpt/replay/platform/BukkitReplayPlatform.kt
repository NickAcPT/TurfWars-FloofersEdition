package io.github.nickacpt.replay.platform

import io.github.nickacpt.behaviours.replay.abstractions.ReplayItemStack
import io.github.nickacpt.behaviours.replay.abstractions.ReplayPlatform
import io.github.nickacpt.behaviours.replay.abstractions.ReplayViewer
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object BukkitReplayPlatform : ReplayPlatform<ItemStack, Player> {

    override fun convertIntoReplayStack(stack: ItemStack): ReplayItemStack {
        TODO("Not yet implemented")
    }

    override fun convertIntoPlatformStack(stack: ReplayItemStack): ItemStack {
        TODO("Not yet implemented")
    }

    override fun convertIntoReplayViewer(viewer: Player): ReplayViewer {
        return BukkitReplayViewer(viewer.uniqueId)
    }

    override fun convertIntoPlatformViewer(viewer: ReplayViewer): Player? {
        return Bukkit.getPlayer(viewer.id)
    }
}