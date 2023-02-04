package io.github.nickacpt.event.core.tags

import io.github.nickacpt.event.core.CorePlugin.Companion.logger
import io.github.nickacpt.event.core.io.DatabaseTagsFunctions
import io.github.nickacpt.event.database.getDatabaseProxy

object TagsManager {

    private val tagsIo by lazy { getDatabaseProxy<DatabaseTagsFunctions>() }

    private val tagsMutable = mutableListOf<PlayerTag>()
    val tags: List<PlayerTag> = tagsMutable

    fun loadTags() {
        logger.info("Loading tags...")

        tagsIo.getTags().forEach { tag ->
            logger.info("Registering ${if (!tag.adminOnly) "" else "admin-only "}tag \"${tag.name}\"")
            tagsMutable.add(tag)
        }

        logger.info("Loaded ${tags.size} tags!")
    }

    fun findByName(currentTagName: String?): PlayerTag? {
        if (currentTagName == null) return null
        return tags.firstOrNull { it.name == currentTagName }
    }

}