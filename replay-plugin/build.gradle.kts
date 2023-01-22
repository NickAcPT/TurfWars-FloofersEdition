import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

dependencies {
    implementation("io.github.nickacpt.behaviours:replay")
}

bukkit {
    name = "Replay"
    main = "io.github.nickacpt.replay.ReplayPlugin"
}

tasks.withType<ShadowJar> {
    tasks.build.get().dependsOn(this)
    archiveClassifier.set("")
}