import net.minecrell.pluginyml.bukkit.BukkitPluginDescription.PluginLoadOrder

bukkit {
    main = "io.github.nickacpt.event.core.CorePlugin"
    load = PluginLoadOrder.STARTUP

    commands {
        register("locraw") {
            description = "LocRaw command for badly coded mods"
        }
    }
}

dependencies {
    library(api("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.0")!!)
    library(api("com.fasterxml.jackson.dataformat:jackson-dataformat-toml:2.14.0")!!)

    library(api("net.kyori.moonshine:moonshine-standard:2.0.4")!!)

    library(api("cloud.commandframework", "cloud-paper", "1.8.0")!!)
    library(api("cloud.commandframework", "cloud-annotations", "1.8.0")!!)
}