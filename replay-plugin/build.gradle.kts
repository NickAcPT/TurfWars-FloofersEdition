import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

repositories {
    maven("https://maven.citizensnpcs.co/repo")
}

dependencies {
    implementation("io.github.nickacpt.behaviours:replay")
    compileOnly(library("cloud.commandframework", "cloud-paper", "1.8.0"))
    compileOnly(library("cloud.commandframework", "cloud-annotations", "1.8.0"))
    compileOnly("net.citizensnpcs", "citizens-main", "2.0.30-SNAPSHOT") {
        exclude(group = "*", module = "*")
    }
}

bukkit {
    name = "Replay"
    main = "io.github.nickacpt.replay.ReplayPlugin"

    depend = listOf("Citizens")
}

tasks.withType<ShadowJar> {
    tasks.build.get().dependsOn(this)
    archiveClassifier.set("")
}