package io.github.nickacpt.replay.platform.abstractions.entity

import com.destroystokyo.paper.profile.ProfileProperty
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

data class PlayerGameProfile(
    val name: String,
    val textures: String,
    val signature: String
) {
    fun toBukkitProfile() = Bukkit.createProfile(UUID.randomUUID(), name).also {
        it.setProperty(ProfileProperty("textures", textures, signature))
    }

    companion object {
        fun fromBukkitProfile(profile: Player): PlayerGameProfile {
            val textures = profile.playerProfile.takeIf { it.hasTextures() }
                ?.properties?.firstOrNull { it.name == "textures" }

            return PlayerGameProfile(
                profile.name,
                textures?.value ?: "",
                textures?.signature ?: ""
            )
        }
    }
}