package io.github.nickacpt.event.core.utils

import com.fasterxml.jackson.dataformat.toml.TomlMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import org.bukkit.plugin.java.JavaPlugin
import kotlin.reflect.KProperty

class ConfigurationFileProvider<T>(private val clazz: Class<T>) {
    companion object {
        val mapper: TomlMapper = TomlMapper.builder()
            .addModule(kotlinModule())
            .build()
    }

    operator fun getValue(plugin: JavaPlugin, property: KProperty<*>): T {
        plugin.dataFolder.mkdirs()
        val file = plugin.dataFolder.resolve("${property.name}.toml")

        if (!file.exists()) {
            // Write default config from resources
            plugin.saveResource("${property.name}.toml", false)
        }

        return mapper.readValue(file, clazz)
    }
}

