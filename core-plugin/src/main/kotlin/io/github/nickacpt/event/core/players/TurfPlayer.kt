package io.github.nickacpt.event.core.players

import io.github.nickacpt.event.core.tags.PlayerTag
import io.github.nickacpt.event.core.tags.TagsManager
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.audience.ForwardingAudience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.Style
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

class TurfPlayer(val uuid: UUID) : ForwardingAudience.Single {
    val displayName: Component
        get() = Component.text {
            it.append(currentTag.value)
            it.mergeStyle(currentTag.value, Style.Merge.COLOR)
            it.appendSpace()
            bukkitPlayer?.name()?.let { name -> it.append(name) }
        }
    var currentTag: PlayerTag = TagsManager.tags.first()

    val bukkitPlayer: Player?
        get() = Bukkit.getPlayer(uuid)

    override fun audience(): Audience {
        return bukkitPlayer ?: Audience.empty()
    }
}