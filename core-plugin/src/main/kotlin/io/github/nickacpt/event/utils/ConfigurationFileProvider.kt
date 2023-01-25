package io.github.nickacpt.event.utils

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.dataformat.toml.TomlMapper
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import com.fasterxml.jackson.module.kotlin.kotlinModule
import io.github.nickacpt.event.config.i18n.I18nConfiguration
import org.bukkit.plugin.java.JavaPlugin
import kotlin.reflect.KProperty

class ConfigurationFileProvider<T>(private val clazz: Class<T>) {
    companion object {
        val mapper: TomlMapper = TomlMapper.builder()
            .addModule(kotlinModule())
            .propertyNamingStrategy(PropertyNamingStrategies.KEBAB_CASE)
            .build()

        val globalStaticPlaceholders = mutableSetOf<Map.Entry<String, String>>()
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

        return try {
            mapper.readValue(file, clazz)
        } catch (e: Exception) {
            val newMap = mapper.readValue(file, jacksonTypeRef<Map<String, Any>>())
            (I18nConfiguration(mutableMapOf(), flattenMap(newMap)) as? T) ?: throw e
        }.also { result ->
            cachedValue = result
            if (result is I18nConfiguration) {
                result.staticPlaceholders.putAll(globalStaticPlaceholders.map { it.key to it.value })
                globalStaticPlaceholders.addAll(result.staticPlaceholders.entries)
            }
        }
    }
}

fun flattenMap(map: Map<String, Any>): Map<String, String> {
    val result = mutableMapOf<String, String>()
    for ((key, value) in map) {
        if (value is Map<*, *>) {
            for ((subKey, subValue) in flattenMap(value as Map<String, Any>)) {
                result["$key.$subKey"] = subValue
            }
        } else {
            result[key] = value.toString()
        }
    }
    return result
}