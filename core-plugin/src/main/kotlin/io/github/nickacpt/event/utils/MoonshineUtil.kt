package io.github.nickacpt.event.utils

import io.github.nickacpt.event.config.i18n.I18nConfiguration
import io.github.nickacpt.event.utils.messages.AudienceMessagerSender
import io.github.nickacpt.event.utils.messages.AudienceReceiverLocator
import io.github.nickacpt.event.utils.messages.SimpleMessageRenderer
import io.github.nickacpt.event.utils.messages.SimplePlaceholderResolver
import io.leangen.geantyref.TypeToken
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.moonshine.Moonshine
import net.kyori.moonshine.strategy.StandardPlaceholderResolverStrategy
import net.kyori.moonshine.strategy.supertype.StandardSupertypeThenInterfaceSupertypeStrategy
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

fun <T> simplePlaceholderResolver(resolver: (T) -> Component): SimplePlaceholderResolver<T> {
    return SimplePlaceholderResolver(resolver = resolver)
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
        .weightedPlaceholderResolver(Player::class.java, simplePlaceholderResolver { it.turfPlayer.getDisplayName() }, 0)
        .weightedPlaceholderResolver(String::class.java, simplePlaceholderResolver { Component.text(it) }, 0)
        .weightedPlaceholderResolver(Component::class.java, simplePlaceholderResolver { it }, 0)

        // We finally create the proxy instance.
        .create(this.javaClass.classLoader)
}

