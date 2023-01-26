package io.github.nickacpt.event.utils

import io.github.nickacpt.event.config.i18n.I18nConfiguration
import io.github.nickacpt.event.core.players.TurfPlayer
import io.github.nickacpt.event.utils.messages.AudienceMessagerSender
import io.github.nickacpt.event.utils.messages.AudienceReceiverLocator
import io.github.nickacpt.event.utils.messages.SimpleMessageRenderer
import io.github.nickacpt.event.utils.messages.SimplePlaceholderResolver
import io.leangen.geantyref.TypeToken
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.moonshine.Moonshine
import net.kyori.moonshine.MoonshineBuilder
import net.kyori.moonshine.strategy.StandardPlaceholderResolverStrategy
import net.kyori.moonshine.strategy.supertype.StandardSupertypeThenInterfaceSupertypeStrategy
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

fun <T> simplePlaceholderResolver(resolver: (T) -> Component): SimplePlaceholderResolver<T> {
    return SimplePlaceholderResolver(resolver = resolver)
}

inline fun <reified T> JavaPlugin.moonshine(name: String): T {
    return moonshine(ConfigurationFileProvider(I18nConfiguration::class.java).getValueByName(this, name))
}

inline fun <reified T> JavaPlugin.moonshine(configuration: I18nConfiguration): T {
    return Moonshine.builder<T, Audience>(TypeToken.get(T::class.java))
        // A receiver locator is used to find the receiver of a message.
        .receiverLocatorResolver({ _, _ -> AudienceReceiverLocator() }, 0)

        // A source is the original message from the configuration.
        .sourced { _, messageKey -> configuration.messages.getOrDefault(messageKey, messageKey) }

        // The renderer is used to take that source message and render it to the receiver, in this case, a chat component.
        .rendered(SimpleMessageRenderer(configuration))

        // This is used to send messages to the receiver.
        .sent(AudienceMessagerSender)

        // This is used to resolve placeholders in the message. I have no clue how this works, but it does.
        .resolvingWithStrategy(StandardPlaceholderResolverStrategy(StandardSupertypeThenInterfaceSupertypeStrategy(false)))

        // This is used to resolve placeholders in the message. These take the parameters of the method and resolve them into a component.
        .placeholderResolver(Component::class.java) { it }
        .placeholderResolver(Player::class.java) { it.turfPlayer.getDisplayName() }
        .placeholderResolver(TurfPlayer::class.java) { it.getDisplayName() }
        .placeholderResolver(String::class.java) { Component.text(it) }
        .placeholderResolver(Int::class.java) { Component.text(it) }
        .placeholderResolver(java.lang.Integer::class.java) { Component.text(it.toInt()) }

        // We finally create the proxy instance.
        .create(this.javaClass.classLoader)
}

fun <T, I, O, Z> MoonshineBuilder.Resolved<T, Audience, I, O, Component>.placeholderResolver(resolvedType: Class<out Z>, resolver: (Z) -> Component): MoonshineBuilder.Resolved<T, Audience, I, O, Component> {
    return this.weightedPlaceholderResolver(resolvedType, simplePlaceholderResolver(resolver), 0)
}
