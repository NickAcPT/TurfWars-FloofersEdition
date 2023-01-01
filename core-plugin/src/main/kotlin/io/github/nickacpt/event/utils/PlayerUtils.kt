package io.github.nickacpt.event.utils

import io.github.nickacpt.event.core.players.TurfPlayer
import io.github.nickacpt.event.core.players.TurfPlayerManager
import org.bukkit.GameMode
import org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerTeleportEvent

val Player.turfPlayer: TurfPlayer
    get() = TurfPlayerManager.getTurfPlayer(uniqueId)

fun Player.refreshPlayer(teleportToSpawn: Boolean, canFly: Boolean?) {
    // Set the player's tag
    displayName(turfPlayer.displayName)

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
}
