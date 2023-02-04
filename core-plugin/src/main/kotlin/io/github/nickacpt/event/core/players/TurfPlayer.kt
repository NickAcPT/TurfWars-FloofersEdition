package io.github.nickacpt.event.core.players

import io.github.nickacpt.event.core.display.events.TurfPlayerRefreshEvent
import io.github.nickacpt.event.core.display.scoreboard.TrackingPlayerScoreboardDisplayData
import io.github.nickacpt.event.core.tags.PlayerTag
import io.github.nickacpt.event.core.tags.TagsManager
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.audience.ForwardingAudience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scoreboard.Scoreboard
import java.util.*

class TurfPlayer(val uuid: UUID, val data: TurfPlayerData) : ForwardingAudience.Single {
    private val tempData = mutableMapOf<TurfPlayerDataKey<*>, Any>()

    internal val trackingSidebarData = TrackingPlayerScoreboardDisplayData(null, null)

    var currentTag: PlayerTag
        get() = data.currentTag ?: TagsManager.tags.first()
        set(value) {
            data.currentTag = value
        }

    val name get() = bukkitPlayer?.name ?: "Unknown($uuid)"

    val bukkitPlayer: Player?
        get() = Bukkit.getPlayer(uuid)

    internal var bukkitScoreboard: Scoreboard? = null
        private set(value) {
            field = value
            bukkitPlayer?.scoreboard = value ?: Bukkit.getScoreboardManager().mainScoreboard
        }

    fun initializeBukkitScoreboard() {
        if (bukkitScoreboard == null) {
            bukkitScoreboard = Bukkit.getScoreboardManager().newScoreboard
        }
    }

    fun getDisplayName(): Component {
        val team = bukkitScoreboard?.getTeam(name)

        val playerName = bukkitPlayer?.name() ?: Component.empty()
        val color = team?.color() ?: NamedTextColor.WHITE
        val prefix = team?.prefix() ?: Component.empty()
        val suffix = team?.suffix() ?: Component.empty()

        return prefix.append(playerName.style(Style.style(color).decorations(TextDecoration.values().toSet(), false)))
            .append(suffix)
    }

    override fun audience(): Audience {
        return bukkitPlayer ?: Audience.empty()
    }

    operator fun <T : Any> get(key: TurfPlayerDataKey<T>): T? {
        return tempData[key] as? T?
    }

    operator fun <T : Any> set(key: TurfPlayerDataKey<T>, value: T?) {
        if (value == null) {
            tempData.remove(key)
        } else {
            tempData[key] = value as Any
        }
    }

    fun refresh() {
        TurfPlayerRefreshEvent(this).callEvent()
    }
}