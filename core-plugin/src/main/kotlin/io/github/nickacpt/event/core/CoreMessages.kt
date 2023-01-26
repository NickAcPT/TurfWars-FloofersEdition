package io.github.nickacpt.event.core

import io.github.nickacpt.event.core.players.TurfPlayer
import io.github.nickacpt.event.utils.messages.annotations.Receiver
import net.kyori.adventure.text.Component
import net.kyori.moonshine.annotation.Message
import net.kyori.moonshine.annotation.Placeholder
import org.bukkit.entity.Player

interface CoreMessages {

    @Message("chat-message")
    fun chatMessage(
        @Placeholder("player") player: Player,
        @Placeholder("message") message: Component
    ): Component

    @Message("help-message")
    fun helpMessage(@Receiver player: TurfPlayer)

}