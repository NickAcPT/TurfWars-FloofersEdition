package io.github.nickacpt.event.utils

import io.github.nickacpt.event.core.CorePlugin
import io.github.nickacpt.event.core.display.events.TurfPlayerRefreshEvent
import io.github.nickacpt.event.core.players.TurfPlayer
import io.github.nickacpt.event.core.players.TurfPlayerManager
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH
import org.bukkit.entity.Player

val Player.turfPlayer: TurfPlayer
    get() = TurfPlayerManager.getOrLoadTurfPlayer(uniqueId)

fun Player.refreshPlayer(
    teleportToSpawn: Boolean,
    canFly: Boolean? = null,
    playerGameMode: GameMode = GameMode.ADVENTURE
) {
    TurfPlayerRefreshEvent(turfPlayer).callEvent()

    playerListName(turfPlayer.getDisplayName())

    // Set the player's gamemode to adventure
    gameMode = playerGameMode

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
        Bukkit.getScheduler().runTaskLater(CorePlugin.instance, Runnable { teleport(world.spawnLocation) }, 1)
    }
}
