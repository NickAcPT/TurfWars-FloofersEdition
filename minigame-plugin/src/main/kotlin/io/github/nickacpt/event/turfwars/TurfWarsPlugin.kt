package io.github.nickacpt.event.turfwars

import io.github.nickacpt.event.core.display.PlayerDisplayManager
import io.github.nickacpt.event.turfwars.commands.game.GameManagementCommands
import io.github.nickacpt.event.turfwars.commands.raw.RawInformationCommands
import io.github.nickacpt.event.turfwars.config.MinigameLocale
import io.github.nickacpt.event.turfwars.config.TurfWarsConfig
import io.github.nickacpt.event.turfwars.display.TurfWarsDisplayProvider
import io.github.nickacpt.event.turfwars.events.PlayerEvents
import io.github.nickacpt.event.turfwars.minigame.GameManager
import io.github.nickacpt.event.turfwars.minigame.events.InGameEvents
import io.github.nickacpt.event.turfwars.minigame.events.LobbyEvents
import io.github.nickacpt.event.utils.TurfCommandManager
import io.github.nickacpt.event.utils.getConfigurationFileProvider
import io.github.nickacpt.event.utils.moonshine
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class TurfWarsPlugin : JavaPlugin() {

    companion object {
        val instance: TurfWarsPlugin
            get() = getPlugin(TurfWarsPlugin::class.java)

        val locale: MinigameLocale
            get() = instance.locale

        val config: TurfWarsConfig
            get() = instance.config

        val testGame by lazy { GameManager.createGame(UUID.randomUUID()) }
    }

    val config by getConfigurationFileProvider<TurfWarsConfig>()
    val locale = moonshine<MinigameLocale>("i18n")

    override fun onEnable() {
        val commandManager = TurfCommandManager(this)
        commandManager.parseCommands(RawInformationCommands, GameManagementCommands)

        Bukkit.getPluginManager().registerEvents(PlayerEvents, this)
        Bukkit.getPluginManager().registerEvents(LobbyEvents, this)
        Bukkit.getPluginManager().registerEvents(InGameEvents, this)
        PlayerDisplayManager.registerProvider(TurfWarsDisplayProvider)

        GameManager.startGameTicking()
    }
}