package io.github.nickacpt.event.turfwars

import io.github.nickacpt.event.core.display.PlayerDisplayManager
import io.github.nickacpt.event.turfwars.config.TurfWarsConfig
import io.github.nickacpt.event.turfwars.display.TurfWarsDisplayProvider
import io.github.nickacpt.event.turfwars.handlers.PlayerEvents
import io.github.nickacpt.event.turfwars.i18n.MinigameLocale
import io.github.nickacpt.event.turfwars.minigame.GameManager
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
        Bukkit.getPluginManager().registerEvents(PlayerEvents, this)
        PlayerDisplayManager.registerProvider(TurfWarsDisplayProvider)

        GameManager.startGameTicking()
    }
}