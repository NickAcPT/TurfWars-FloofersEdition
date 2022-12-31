package io.github.nickacpt.event.lobby

import io.github.nickacpt.event.lobby.handlers.PlayerEvents
import io.github.nickacpt.event.lobby.handlers.WorldEvents
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class LobbyPlugin : JavaPlugin() {
    override fun onEnable() {
        Bukkit.getPluginManager().registerEvents(PlayerEvents, this)
        Bukkit.getPluginManager().registerEvents(WorldEvents, this)
    }
}