package io.github.nickacpt.event.turfwars.utils

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.audience.ForwardingAudience
import net.kyori.adventure.audience.MessageType
import net.kyori.adventure.chat.ChatType
import net.kyori.adventure.chat.SignedMessage
import net.kyori.adventure.identity.Identified
import net.kyori.adventure.identity.Identity
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit

open class PrefixedConsoleProxyAudience(prefix: String) : ForwardingAudience.Single {
    private val prefix = Component.text(prefix).append(Component.space())

    private fun prefixMessage(message: Component): Component {
        return prefix.append(message)
    }

    override fun sendActionBar(message: Component) {
        sendMessage(message)
    }

    override fun sendMessage(message: Component) {
        super.sendMessage(prefixMessage(message))
    }

    override fun sendMessage(message: Component, boundChatType: ChatType.Bound) {
        super.sendMessage(prefixMessage(message), boundChatType)
    }

    override fun sendMessage(source: Identified, message: Component, type: MessageType) {
        super.sendMessage(source, prefixMessage(message), type)
    }

    override fun sendMessage(source: Identity, message: Component, type: MessageType) {
        super.sendMessage(source, prefixMessage(message), type)
    }

    override fun audience(): Audience {
        return Bukkit.getConsoleSender()
    }
}