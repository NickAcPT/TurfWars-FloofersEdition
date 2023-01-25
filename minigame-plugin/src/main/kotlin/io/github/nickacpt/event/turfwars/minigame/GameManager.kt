package io.github.nickacpt.event.turfwars.minigame

import io.github.nickacpt.event.turfwars.TurfWarsPlugin
import org.bukkit.Bukkit
import java.util.*

object GameManager {
    private val games = mutableMapOf<UUID, TurfWarsGame>()

    fun startGameTicking() {
        Bukkit.getScheduler().runTaskTimer(TurfWarsPlugin.instance, Runnable {
            games.values.forEach { it.tick() }
        }, 10, 1)
    }

    fun getGame(id: UUID): TurfWarsGame? {
        return games[id]
    }

    fun createGame(id: UUID): TurfWarsGame {
        val game = TurfWarsGame(id)
        games[id] = game
        return game
    }

    fun removeGame(id: UUID) {
        games.remove(id)
    }

}