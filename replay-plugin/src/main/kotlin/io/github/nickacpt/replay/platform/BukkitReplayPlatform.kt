package io.github.nickacpt.replay.platform

import io.github.nickacpt.behaviours.replay.abstractions.ReplayItemStack
import io.github.nickacpt.behaviours.replay.abstractions.ReplayPlatform
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object BukkitReplayPlatform : ReplayPlatform<ItemStack> {

    override fun convertIntoPlatformStack(stack: ReplayItemStack): ItemStack {
        return ItemStack(Material.AIR)
    }

    override fun convertIntoReplayStack(stack: ItemStack): ReplayItemStack {
        TODO("Not yet implemented")
    }
}