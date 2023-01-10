package io.github.nickacpt.event.core

import io.github.nickacpt.event.config.i18n.I18nConfiguration
import io.github.nickacpt.event.core.commands.LocRawCommand
import io.github.nickacpt.event.core.config.CoreConfig
import io.github.nickacpt.event.core.display.PlayerDisplayManager
import io.github.nickacpt.event.core.handlers.ChatEvents
import io.github.nickacpt.event.core.handlers.WorldEvents
import io.github.nickacpt.event.core.players.TurfPlayerManager
import io.github.nickacpt.event.core.tags.TagsManager
import io.github.nickacpt.event.utils.getConfigurationFileProvider
import io.github.nickacpt.event.utils.moonshine
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class CorePlugin : JavaPlugin() {

    companion object {
        val instance: CorePlugin get() = getPlugin(CorePlugin::class.java)
        val logger get() = instance.logger
    }

    val config: CoreConfig by getConfigurationFileProvider()

    val i18n: I18nConfiguration by getConfigurationFileProvider()
    val messages: CoreMessages = moonshine(i18n)

    override fun onEnable() {
        // Register locraw command
        getCommand("locraw")?.setExecutor(LocRawCommand)

        // Loads all tags
        TagsManager.loadTags()

        // Register events
        Bukkit.getPluginManager().registerEvents(ChatEvents, this)
        Bukkit.getPluginManager().registerEvents(ChatEvents, this)
        Bukkit.getPluginManager().registerEvents(WorldEvents, this)
        Bukkit.getPluginManager().registerEvents(TurfPlayerManager, this)
        Bukkit.getPluginManager().registerEvents(PlayerDisplayManager, this)
    }

    override fun onDisable() {
        // Save all player data
        TurfPlayerManager.saveAll()
    }
}