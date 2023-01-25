package io.github.nickacpt.event.turfwars

import io.github.nickacpt.event.turfwars.handlers.PlayerEvents
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class TurfWarsPlugin : JavaPlugin() {

    override fun onEnable() {
        Bukkit.getPluginManager().registerEvents(PlayerEvents, this)
    }
}