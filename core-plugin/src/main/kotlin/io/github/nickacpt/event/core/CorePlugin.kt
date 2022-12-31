package io.github.nickacpt.event.core

import io.github.nickacpt.event.core.handlers.WorldEvents
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class CorePlugin : JavaPlugin() {

    override fun onEnable() {
        Bukkit.getPluginManager().registerEvents(WorldEvents, this)
    }

}