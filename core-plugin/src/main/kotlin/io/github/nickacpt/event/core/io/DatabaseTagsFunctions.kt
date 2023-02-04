package io.github.nickacpt.event.core.io

import io.github.nickacpt.event.core.tags.PlayerTag
import io.github.nickacpt.event.database.DatabaseMethod

interface DatabaseTagsFunctions {

    @DatabaseMethod("core_get_tags")
    fun getTags(): List<PlayerTag>

    @DatabaseMethod("core_add_tag")
    fun addTag(name: String, value: String, adminOnly: Boolean)

    @DatabaseMethod("core_remove_tag")
    fun removeTag(tag: PlayerTag)

}