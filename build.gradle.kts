import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


group = "net.axay"
version = "1.8.0"

description = "A Kotlin API for the Minecraft Server Software \"Spigot\"."

plugins {
    kotlin("jvm") version "1.7.0"
    `maven-publish`
    kotlin("plugin.serialization") version "1.7.0"
}

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    maven("https://repo.codemc.io/repository/maven-snapshots/")
    mavenLocal() // to get the locally available binaries of spigot (use the BuildTools)
}

dependencies {
    compileOnly("org.spigotmc", "spigot-api", "1.8.8-R0.1-SNAPSHOT")
    compileOnly("org.bukkit", "craftbukkit", "1.8.8-R0.1-SNAPSHOT")
    implementation("net.wesjd:anvilgui:1.5.3-SNAPSHOT")
    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.2")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.1")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.5.1")
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release.set(8)
    }

    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }
}

publishing {
    publications {
        create<MavenPublication>(project.name) {
            from(components["java"])
            this.groupId = project.group.toString()
            this.artifactId = project.name.toLowerCase()
            this.version = project.version.toString()
        }
    }
}
