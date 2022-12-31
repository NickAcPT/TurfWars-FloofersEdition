import net.minecrell.pluginyml.bukkit.BukkitPluginDescription.PluginLoadOrder

bukkit {
    main = "io.github.nickacpt.event.core.CorePlugin"
    load = PluginLoadOrder.STARTUP
}

dependencies {
    library(api("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.0")!!)
    library(api("com.fasterxml.jackson.dataformat:jackson-dataformat-toml:2.14.0")!!)

    library(api("net.kyori.moonshine:moonshine-standard:2.0.4")!!)
}