import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.21" apply false
}

allprojects {
    group = "io.github.nickacpt"
    version = "1.0-SNAPSHOT"

    this.apply(plugin = "org.jetbrains.kotlin.jvm")

    this.repositories {
        mavenCentral()
    }

    this.dependencies {
        "implementation"(kotlin("stdlib"))
    }

    this.tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "18"
    }
}