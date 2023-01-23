package io.github.nickacpt.replay.platform

import com.destroystokyo.paper.profile.ProfileProperty
import io.github.nickacpt.behaviours.replay.ReplaySystem
import io.github.nickacpt.behaviours.replay.logic.ReplayControlItemType
import io.github.nickacpt.behaviours.replay.playback.session.ReplaySession
import io.github.nickacpt.replay.ReplayPlugin
import io.github.nickacpt.replay.platform.abstractions.BukkitReplayViewer
import io.github.nickacpt.replay.platform.abstractions.BukkitReplayWorld
import io.github.nickacpt.replay.platform.abstractions.entity.BukkitReplayEntity
import io.github.nickacpt.replay.platform.utils.ReplayItemHeadTextures
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.persistence.PersistentDataType
import java.util.*

object ReplayItemStackUtils {
    private val CONTROL_TYPE_KEY = NamespacedKey(ReplayPlugin.instance, "replay_control_type")

    fun createControlItemStack(
        control: ReplayControlItemType,
        session: ReplaySession<BukkitReplayViewer, BukkitReplayWorld, BukkitReplayEntity, BukkitReplayPlatform, ReplaySystem<BukkitReplayViewer, BukkitReplayWorld, BukkitReplayEntity, BukkitReplayPlatform>>
    ): ItemStack {
        val material = when (control) {
            ReplayControlItemType.PAUSE -> Material.PINK_DYE
            ReplayControlItemType.RESUME -> Material.GRAY_DYE
            ReplayControlItemType.RESTART -> Material.LIME_DYE

            else -> Material.PLAYER_HEAD
        }

        val title = Component.text(
            when (control) {
                ReplayControlItemType.DECREASE_SPEED -> "Decrease Speed"
                ReplayControlItemType.STEP_BACKWARDS -> "${session.settings.currentStepSize.inWholeSeconds}s Backwards"
                ReplayControlItemType.PAUSE -> "Click to Pause"
                ReplayControlItemType.RESUME -> "Click to Resume"
                ReplayControlItemType.RESTART -> "Play recording again"
                ReplayControlItemType.STEP_FORWARD -> "${session.settings.currentStepSize.inWholeSeconds}s Forward"
                ReplayControlItemType.INCREASE_SPEED -> "Increase Speed"
            }
        , NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)

        return ItemStack(material, 1).apply {
            val itemSkin = when (control) {

                ReplayControlItemType.DECREASE_SPEED -> ReplayItemHeadTextures.DECREASE_SPEED
                ReplayControlItemType.STEP_BACKWARDS -> ReplayItemHeadTextures.SKIP_BACKWARDS

                ReplayControlItemType.STEP_FORWARD -> ReplayItemHeadTextures.SKIP_FORWARDS
                ReplayControlItemType.INCREASE_SPEED -> ReplayItemHeadTextures.INCREASE_SPEED
                else -> null
            }
            if (itemSkin != null) {
                editMeta(SkullMeta::class.java) {
                    it.playerProfile = Bukkit.createProfile(UUID(0, 0)).apply {
                        setProperty(ProfileProperty("textures", itemSkin))
                    }
                }
            }

            editMeta {
                val lore = when (control) {
                    ReplayControlItemType.RESUME -> "The replay is currently paused."
                    ReplayControlItemType.RESTART -> "The replay is currently stopped."
                    ReplayControlItemType.STEP_BACKWARDS, ReplayControlItemType.STEP_FORWARD -> "Left click to change the duration."
                    else -> null
                }

                it.displayName(title)
                if (lore != null) it.lore(listOf(Component.text(lore, NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))
            }

            this.controlType = control
        }
    }

    var ItemStack.controlType: ReplayControlItemType?
        get() {
            val meta = this.takeIf { hasItemMeta() }?.itemMeta ?: return null
            val data = meta.persistentDataContainer.get(CONTROL_TYPE_KEY, PersistentDataType.BYTE) ?: return null
            return ReplayControlItemType.values()[data.toInt()]
        }
        private set(value) {
            editMeta {
                if (value == null) {
                    it.persistentDataContainer.remove(CONTROL_TYPE_KEY)
                } else {
                    it.persistentDataContainer.set(CONTROL_TYPE_KEY, PersistentDataType.BYTE, value.ordinal.toByte())
                }
            }
        }
}