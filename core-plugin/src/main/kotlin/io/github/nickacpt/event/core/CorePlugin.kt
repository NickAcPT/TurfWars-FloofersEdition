package io.github.nickacpt.event.core

import io.github.nickacpt.event.config.i18n.I18nConfiguration
import io.github.nickacpt.event.core.commands.FakeBukkitCommands
import io.github.nickacpt.event.core.commands.LocRawCommand
import io.github.nickacpt.event.core.config.CoreConfig
import io.github.nickacpt.event.core.display.PlayerDisplayManager
import io.github.nickacpt.event.core.handlers.ChatEvents
import io.github.nickacpt.event.core.handlers.PlayerMetricEvents
import io.github.nickacpt.event.core.handlers.WorldEvents
import io.github.nickacpt.event.core.metrics.PlayerMetricsManager
import io.github.nickacpt.event.core.players.TurfPlayerManager
import io.github.nickacpt.event.core.tags.TagsManager
import io.github.nickacpt.event.database.Database
import io.github.nickacpt.event.utils.TurfCommandManager
import io.github.nickacpt.event.utils.getConfigurationFileProvider
import io.github.nickacpt.event.utils.moonshine
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.PluginIdentifiableCommand
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask

class CorePlugin : JavaPlugin() {

    companion object {
        val instance: CorePlugin get() = getPlugin(CorePlugin::class.java)
        val logger get() = instance.logger
    }

    val config: CoreConfig by getConfigurationFileProvider()

    val i18n: I18nConfiguration by getConfigurationFileProvider()
    val messages: CoreMessages = moonshine(i18n)

    override fun onEnable() {
        // Initialize database connection
        Database.init(config.database.url, config.database.username, config.database.password)

        // Yeet all the commands from the server
        filterCommandTask = Bukkit.getScheduler().runTaskTimer(
            this,
            ::filterCommands,
            0, 20 /* TICKS */ * 10 /* SECONDS */
        )

        val commandManager = TurfCommandManager(this)
        commandManager.annotationParser.parse(FakeBukkitCommands)

        // Register locraw command
        getCommand("locraw")?.setExecutor(LocRawCommand)

        // Loads all tags
        TagsManager.loadTags()

        // Initialize player metrics manager
        PlayerMetricsManager.init()

        // Register events
        Bukkit.getPluginManager().registerEvents(ChatEvents, this)
        Bukkit.getPluginManager().registerEvents(WorldEvents, this)
        Bukkit.getPluginManager().registerEvents(TurfPlayerManager, this)
        Bukkit.getPluginManager().registerEvents(PlayerMetricEvents, this)
        Bukkit.getPluginManager().registerEvents(PlayerDisplayManager, this)
    }

    private var filterCommandTask: BukkitTask? = null
    private var filterCommandsCount = 0
    private fun filterCommands() {
        if (filterCommandsCount++ > 5) {
            filterCommandTask?.cancel()
            return
        }

        val commandMap = Bukkit.getCommandMap()
        val knownCommands = commandMap.knownCommands
        val knownCommandsClone = knownCommands.toList() // Clone the list to avoid concurrent modification

        val toKeep = setOf(
            "op", "deop", "ping", "give", "teleport",
            "tp", "enchant", "time", "gamemode", "kick",
            "ban", "pardon", "mspt", "tps", "tpsbar", "stop",
            "version", "save-all", "save-off", "save-on",
            "whitelist", "plugins"
        )
        val commandsToKeep = mutableMapOf<String, Command>()

        knownCommandsClone.forEach { (key, value) ->
            if (value is PluginIdentifiableCommand || toKeep.contains(key) || toKeep.contains(value.name)) {
                commandsToKeep[key] = value
                return@forEach
            }

            value.unregister(commandMap)
            knownCommands.remove(key)
        }

        knownCommands.putAll(commandsToKeep)

        Bukkit.getServer().javaClass.getMethod("syncCommands").invoke(Bukkit.getServer())
    }

    override fun onDisable() {
        // Save all player data
        TurfPlayerManager.saveAll()
    }
}