package io.github.nickacpt.event.core.utils.i18n

import io.github.nickacpt.event.core.config.i18n.I18nConfiguration
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import net.kyori.moonshine.message.IMessageRenderer
import java.lang.reflect.Method
import java.lang.reflect.Type

class SimpleMessageRenderer(private val configuration: I18nConfiguration) : IMessageRenderer<Audience, String, Component, Component> {
    override fun render(
        receiver: Audience?,
        intermediateMessage: String?,
        resolvedPlaceholders: MutableMap<String, out Component>?,
        method: Method?,
        owner: Type?
    ): Component {
        val tagResolver = TagResolver.builder()

        resolvedPlaceholders?.forEach { (key, value) ->
            tagResolver.resolver(Placeholder.component(key, value))
        }

        configuration.staticPlaceholders.forEach { (key, value) ->
            tagResolver.resolver(Placeholder.parsed(key, value))
        }

        return MiniMessage.miniMessage().deserialize(intermediateMessage ?: "unknown message", tagResolver.build())
    }
}