package io.github.nickacpt.event.core.tags

import io.github.nickacpt.event.core.CorePlugin
import io.github.nickacpt.event.core.CorePlugin.Companion.logger
import net.kyori.adventure.text.minimessage.MiniMessage.miniMessage

object TagsManager {

    private val tagsMutable = mutableListOf<PlayerTag>()
    val tags: List<PlayerTag> = tagsMutable

    fun loadTags() {
        val config = CorePlugin.instance.config

        logger.info("Loading tags...")
        config.tags.forEach { (key, value) -> registerTag(key, value, false) }
        config.adminTags.forEach { (key, value) -> registerTag(key, value, true) }
        logger.info("Loaded ${tags.size} tags!")
    }

    private fun registerTag(key: String, value: String, adminOnly: Boolean) {
        logger.info("Registering ${if (!adminOnly) "" else "admin-only "}tag \"$key\"")
        tagsMutable.add(PlayerTag(key, miniMessage().deserialize(value), adminOnly))
    }

    fun findByName(currentTagName: String?): PlayerTag? {
        if (currentTagName == null) return null
        return tags.firstOrNull { it.name == currentTagName }
    }

}