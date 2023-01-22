package io.github.nickacpt.replay.platform

import io.github.nickacpt.behaviours.replay.items.ReplayControlItemType
import io.github.nickacpt.replay.ReplayPlugin
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object ReplayItemStackUtils {
    private val CONTROL_TYPE_KEY = NamespacedKey(ReplayPlugin.instance, "replay_control_type")

    fun createControlItemStack(control: ReplayControlItemType): ItemStack {
        val material = Material.GRAY_DYE

        return ItemStack(material, 1).apply {
            controlType = control
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