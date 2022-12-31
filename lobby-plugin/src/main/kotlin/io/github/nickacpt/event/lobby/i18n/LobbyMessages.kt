package io.github.nickacpt.event.lobby.i18n

import net.kyori.adventure.text.Component
import net.kyori.moonshine.annotation.Message
import net.kyori.moonshine.annotation.Placeholder
import org.bukkit.entity.Player

interface LobbyMessages {
    @Message("player-joined")
    fun playerJoinMessage(@Placeholder("player") player: Player): Component?
}