package io.github.nickacpt.event.core.players

import io.github.nickacpt.event.core.tags.PlayerTag
import io.github.nickacpt.event.core.tags.TagsManager
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.audience.ForwardingAudience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scoreboard.Scoreboard
import java.util.*

class TurfPlayer(val uuid: UUID, val data: TurfPlayerData) : ForwardingAudience.Single {

    val name get() = bukkitPlayer?.name ?: "Unknown"

    fun initializeBukkitScoreboard() {
        if (bukkitScoreboard == null) {
            bukkitScoreboard = Bukkit.getScoreboardManager().newScoreboard
        }
    }

    var bukkitScoreboard: Scoreboard? = null
        private set(value) {
            field = value
            bukkitPlayer?.scoreboard = value ?: Bukkit.getScoreboardManager().mainScoreboard
        }

    var currentTag: PlayerTag = TagsManager.findByName(data.currentTagName) ?: TagsManager.tags.first()

    val bukkitPlayer: Player?
        get() = Bukkit.getPlayer(uuid)

    fun getDisplayName(): Component {
        val team = bukkitScoreboard?.getTeam(name)

        val playerName = bukkitPlayer?.name() ?: Component.empty()
        val color = team?.color() ?: NamedTextColor.WHITE
        val prefix = team?.prefix() ?: Component.empty()
        val suffix = team?.suffix() ?: Component.empty()

        return prefix.append(playerName.color(color)).append(suffix)
    }

    override fun audience(): Audience {
        return bukkitPlayer ?: Audience.empty()
    }
}