package io.github.nickacpt.event.turfwars.config

import org.bukkit.Location
import org.bukkit.World

data class ConfigurationLocation(
    val x: Double,
    val y: Double,
    val z: Double,
    val yaw: Float = 0f,
    val pitch: Float = 0f
) {
    fun toBukkitLocation(world: World): Location {
        return Location(world, x, y, z, yaw, pitch)
    }
}