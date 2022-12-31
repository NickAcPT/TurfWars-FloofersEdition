import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.21" apply false
    id("net.minecrell.plugin-yml.bukkit") version "0.5.2" apply false
}

allprojects {
    group = "io.github.nickacpt"
    version = "1.0-SNAPSHOT"

    val isCore = name.contains("core")
    val isMinecraftPlugin = name.contains("-plugin")

    this.apply(plugin = "org.jetbrains.kotlin.jvm")
    this.apply(plugin = "org.gradle.java-library")
    if (isMinecraftPlugin) this.apply(plugin = "net.minecrell.plugin-yml.bukkit")

    this.repositories {
        mavenCentral()
        if (isMinecraftPlugin) maven("https://repo.papermc.io/repository/maven-public/")
    }

    this.dependencies {
        "implementation"(kotlin("stdlib"))
        if (isMinecraftPlugin) {
            "library"(kotlin("stdlib"))
            "compileOnly"("io.papermc.paper:paper-api:1.19.3-R0.1-SNAPSHOT")
        }
    }

    if (isMinecraftPlugin) {
        this.extensions.getByType(BukkitPluginDescription::class.java).apply {
            apiVersion = "1.19"
            author = "NickAc"

            if (isCore) {
                name = "Core"
            } else {
                depend = listOf("Core")
            }
        }
    }

    this.extensions.getByType(JavaPluginExtension::class.java).apply {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(18))
        }
    }

    this.tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "18"
    }
}