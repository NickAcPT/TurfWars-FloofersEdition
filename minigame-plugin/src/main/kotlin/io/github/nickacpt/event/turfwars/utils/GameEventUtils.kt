package io.github.nickacpt.event.turfwars.utils

import io.github.nickacpt.event.core.players.TurfPlayer
import io.github.nickacpt.event.turfwars.minigame.MinigameState
import io.github.nickacpt.event.turfwars.minigame.TurfWarsGame
import io.github.nickacpt.event.turfwars.minigame.TurfWarsGame.Companion.game
import io.github.nickacpt.event.utils.turfPlayer
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.entity.EntityEvent
import org.bukkit.event.player.PlayerEvent
import java.lang.invoke.MethodHandle
import java.lang.invoke.MethodHandles
import java.lang.invoke.MethodType

val playerFinderCache = mutableMapOf<Class<*>, MethodHandle>()

inline fun Event.handleGameEvent(vararg requiredStates: MinigameState, code: (TurfPlayer, TurfWarsGame) -> Unit) {
    val target = when (this) {
        is PlayerEvent -> {
            this.player
        }
        is EntityEvent -> {
            this.entity as? Player
        }
        else -> {
            try {
                val handle = playerFinderCache.getOrPut(this.javaClass) {
                    MethodHandles.lookup()
                        .findVirtual(this.javaClass, "getPlayer", MethodType.methodType(Player::class.java))
                }

                handle.invoke(this) as Player
            } catch (e: Exception) {
                throw RuntimeException("I don't know how to handle this event!", e)
            }
        }
    }

    target?.turfPlayer?.game?.let {
        if (it.state !in requiredStates) return
        code(target.turfPlayer, it)
    }
}

fun <T> T.cancelEvent(vararg requiredStates: MinigameState)
        where T : Event, T : Cancellable {
    handleGameEvent(*requiredStates) { _, _ ->
        this.isCancelled = true
    }
}