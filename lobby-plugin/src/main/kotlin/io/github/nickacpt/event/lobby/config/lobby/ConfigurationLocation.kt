package io.github.nickacpt.event.lobby.config.lobby

import org.bukkit.Bukkit
import org.bukkit.Location

data class ConfigurationLocation(
    val x: Double,
    val y: Double,
    val z: Double,
    val yaw: Float = 0f,
    val pitch: Float = 0f
) {
    fun toBukkitLocation(): Location {
        return Location(Bukkit.getWorld("world"), x, y, z, yaw, pitch)
    }
}