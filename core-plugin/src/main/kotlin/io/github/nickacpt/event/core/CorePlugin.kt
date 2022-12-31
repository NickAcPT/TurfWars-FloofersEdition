package io.github.nickacpt.event.core

import io.github.nickacpt.event.config.i18n.I18nConfiguration
import io.github.nickacpt.event.core.commands.LocRawCommand
import io.github.nickacpt.event.core.handlers.ChatEvents
import io.github.nickacpt.event.core.handlers.WorldEvents
import io.github.nickacpt.event.utils.getConfigurationFileProvider
import io.github.nickacpt.event.utils.moonshine
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class CorePlugin : JavaPlugin() {

    companion object {
        val instance: CorePlugin get() = getPlugin(CorePlugin::class.java)
    }

    val i18n by getConfigurationFileProvider<I18nConfiguration>()
    val messages: CoreMessages = moonshine(i18n)

    override fun onEnable() {
        // Register locraw command
        getCommand("locraw")?.setExecutor(LocRawCommand)

        // Register events
        Bukkit.getPluginManager().registerEvents(ChatEvents, this)
        Bukkit.getPluginManager().registerEvents(WorldEvents, this)
    }

}