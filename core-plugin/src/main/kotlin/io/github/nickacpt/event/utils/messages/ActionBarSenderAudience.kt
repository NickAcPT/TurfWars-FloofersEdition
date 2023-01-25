package io.github.nickacpt.event.utils.messages

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.audience.ForwardingAudience

data class ActionBarSenderAudience(val audience: Audience) : ForwardingAudience.Single {
    override fun audience(): Audience = audience
}