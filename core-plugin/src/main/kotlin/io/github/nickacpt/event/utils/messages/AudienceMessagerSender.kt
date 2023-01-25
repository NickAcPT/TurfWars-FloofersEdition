package io.github.nickacpt.event.utils.messages

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.moonshine.message.IMessageSender

object AudienceMessagerSender : IMessageSender<Audience, Component> {
    override fun send(receiver: Audience?, renderedMessage: Component) {
        if (receiver is ActionBarSenderAudience) {
            receiver.audience.sendActionBar(renderedMessage)
        } else {
            receiver?.sendMessage(renderedMessage)
        }
    }
}
