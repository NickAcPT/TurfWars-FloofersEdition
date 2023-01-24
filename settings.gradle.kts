pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

rootProject.name = "turf-wars-event"

include("replay-plugin", "core-plugin", "lobby-plugin")

includeBuild("../OrionCraftMc/behaviours")