package io.github.nickacpt.event.lobby

import io.github.nickacpt.event.core.utils.getConfigurationFileProvider
import io.github.nickacpt.event.lobby.config.LobbyConfiguration
import io.github.nickacpt.event.lobby.handlers.PlayerEvents
import io.github.nickacpt.event.lobby.handlers.WorldEvents
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class LobbyPlugin : JavaPlugin() {

    companion object {
        val instance: LobbyPlugin
            get() = getPlugin(LobbyPlugin::class.java)
    }

    val config: LobbyConfiguration by getConfigurationFileProvider()
    override fun onEnable() {
        Bukkit.getPluginManager().registerEvents(PlayerEvents, this)
        Bukkit.getPluginManager().registerEvents(WorldEvents, this)
    }
}