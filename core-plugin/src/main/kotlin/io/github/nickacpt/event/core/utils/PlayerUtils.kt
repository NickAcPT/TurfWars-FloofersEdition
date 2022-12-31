package io.github.nickacpt.event.core.utils

import org.bukkit.GameMode
import org.bukkit.attribute.Attribute.*
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerTeleportEvent

fun Player.resetPlayer(teleportToSpawn: Boolean, canFly: Boolean?) {
    // Set the player's gamemode to adventure
    gameMode = GameMode.ADVENTURE

    // Reset the player's health and food level
    health = getAttribute(GENERIC_MAX_HEALTH)?.value ?: 20.0
    foodLevel = 20
    saturation = 20f

    // Reset the player's experience
    exp = 0f
    totalExperience = 0

    // Reset the player's fire ticks
    fireTicks = 0

    // Set the player's flying state
    if (canFly != null) {
        allowFlight = canFly
        isFlying = canFly
    }

    // Reset the player's inventory
    inventory.clear()
    inventory.setItemInOffHand(null)
    inventory.armorContents = emptyArray()

    // Reset the player's potion effects
    activePotionEffects.forEach { removePotionEffect(it.type) }

    // Reset the player's location
    if (teleportToSpawn) {
        teleport(world.spawnLocation, PlayerTeleportEvent.TeleportCause.PLUGIN)
    }

    // Reset the player's fall distance
    fallDistance = 0f
}
