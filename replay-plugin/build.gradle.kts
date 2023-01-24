import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("io.papermc.paperweight.userdev") version "1.4.0"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

dependencies {
    paperDevBundle("1.19.3-R0.1-SNAPSHOT")
    implementation("io.github.nickacpt.behaviours:replay")
    compileOnly(library("com.esotericsoftware:kryo:5.4.0")!!)
    compileOnly(library("cloud.commandframework", "cloud-paper", "1.8.0"))
    compileOnly(library("cloud.commandframework", "cloud-annotations", "1.8.0"))
}

tasks {
    // Configure reobfJar to run when invoking the build task
    assemble {
        dependsOn(reobfJar)

        doLast {
            val file = this@tasks.getByName<Jar>("jar").destinationDirectory.asFile.get()
            val original = reobfJar.get().outputJar.asFile.get()
            original.copyTo(File(file, original.name), true)

            for (f in file.listFiles()!!) {
                if (f.name.endsWith("-dev.jar") || f.name.endsWith("-dev-all.jar")) {
                    f.delete()
                }
            }
        }
    }
}

bukkit {
    name = "Replay"
    main = "io.github.nickacpt.replay.ReplayPlugin"
}

tasks.withType<ShadowJar> {
    archiveClassifier.set("")
}