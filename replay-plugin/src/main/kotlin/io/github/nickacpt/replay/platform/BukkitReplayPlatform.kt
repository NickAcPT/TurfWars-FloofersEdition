package io.github.nickacpt.replay.platform

import io.github.nickacpt.behaviours.replay.ReplaySystem
import io.github.nickacpt.behaviours.replay.abstractions.ReplayItemStack
import io.github.nickacpt.behaviours.replay.abstractions.ReplayPlatform
import io.github.nickacpt.behaviours.replay.abstractions.ReplayViewer
import io.github.nickacpt.behaviours.replay.abstractions.ReplayWorld
import io.github.nickacpt.behaviours.replay.items.ReplayControlItemType
import io.github.nickacpt.behaviours.replay.model.Replay
import io.github.nickacpt.behaviours.replay.playback.Replayer
import io.github.nickacpt.replay.platform.ItemStackUtils.controlType
import io.github.nickacpt.replay.platform.abstractions.BinarySerializedReplayItemStack
import io.github.nickacpt.replay.platform.abstractions.BukkitReplayViewer
import io.github.nickacpt.replay.platform.abstractions.BukkitReplayWorld
import io.github.nickacpt.replay.platform.abstractions.BukkitReplayerImpl
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object BukkitReplayPlatform : ReplayPlatform<ItemStack, Player, World> {

    override fun convertIntoReplayStack(stack: ItemStack): ReplayItemStack {
        return stack.controlType?.let { return it } ?: BinarySerializedReplayItemStack(stack.serializeAsBytes())
    }

    override fun convertIntoPlatformStack(stack: ReplayItemStack): ItemStack {
        if (stack is BinarySerializedReplayItemStack) {
            return ItemStack.deserializeBytes(stack.bytes).ensureServerConversions()
        }
        else if (stack is ReplayControlItemType) {
            return ItemStackUtils.createControlItemStack(stack)
        }

        throw IllegalArgumentException("Unknown ReplayItemStack type: ${stack::class}")
    }

    override fun convertIntoReplayViewer(viewer: Player): ReplayViewer {
        return BukkitReplayViewer(this, viewer.uniqueId)
    }

    override fun convertIntoPlatformViewer(viewer: ReplayViewer): Player? {
        return Bukkit.getPlayer(viewer.id)
    }

    override fun convertIntoPlatformWorld(world: ReplayWorld): World? {
        return Bukkit.getWorld(world.id)
    }

    override fun convertIntoReplayWorld(world: World): ReplayWorld {
        return BukkitReplayWorld(world.uid)
    }

    override fun <Platform : ReplayPlatform<ItemStack, Player, World>, System : ReplaySystem<ItemStack, Player, World, Platform>> createReplayer(
        replaySystem: System,
        replay: Replay
    ): Replayer<ItemStack, Player, World, Platform, System> {
        return BukkitReplayerImpl()
    }
}