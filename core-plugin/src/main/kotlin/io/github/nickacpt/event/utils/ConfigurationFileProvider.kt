package io.github.nickacpt.event.utils

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.dataformat.toml.TomlMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import org.bukkit.plugin.java.JavaPlugin
import kotlin.reflect.KProperty

class ConfigurationFileProvider<T>(private val clazz: Class<T>) {
    companion object {
        val mapper: TomlMapper = TomlMapper.builder()
            .addModule(kotlinModule())
            .propertyNamingStrategy(PropertyNamingStrategies.KEBAB_CASE)
            .build()
    }

    private var cachedValue: T? = null

    operator fun getValue(plugin: JavaPlugin, property: KProperty<*>): T {
        return getValueByName(plugin, property.name)
    }

    fun getValueByName(plugin: JavaPlugin, name: String): T {
        if (cachedValue != null) return cachedValue!!

        plugin.dataFolder.mkdirs()
        val file = plugin.dataFolder.resolve("$name.toml")

        if (true) {
            // Write default config from resources
            plugin.saveResource("$name.toml", true)
        }

        return mapper.readValue(file, clazz).also { cachedValue = it }
    }
}

