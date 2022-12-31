package io.github.nickacpt.event.lobby

import io.github.nickacpt.event.config.i18n.I18nConfiguration
import io.github.nickacpt.event.lobby.config.lobby.LobbyConfiguration
import io.github.nickacpt.event.lobby.handlers.PlayerEvents
import io.github.nickacpt.event.lobby.handlers.PlayerIoEvents
import io.github.nickacpt.event.lobby.handlers.WorldEvents
import io.github.nickacpt.event.lobby.i18n.LobbyMessages
import io.github.nickacpt.event.utils.getConfigurationFileProvider
import io.github.nickacpt.event.utils.moonshine
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class LobbyPlugin : JavaPlugin() {

    companion object {
        val instance: LobbyPlugin
            get() = getPlugin(LobbyPlugin::class.java)
    }

    val config: LobbyConfiguration by getConfigurationFileProvider()
    val i18n: I18nConfiguration by getConfigurationFileProvider()

    val messages: LobbyMessages = moonshine(i18n)

    override fun onEnable() {
        Bukkit.getPluginManager().registerEvents(WorldEvents, this)
        Bukkit.getPluginManager().registerEvents(PlayerEvents, this)
        Bukkit.getPluginManager().registerEvents(PlayerIoEvents, this)
    }
}