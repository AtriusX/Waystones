import dev.s7a.gradle.minecraft.server.tasks.LaunchMinecraftServerTask

plugins {
    id("java")
    id("idea")
    id("org.jetbrains.kotlin.jvm") version "2.1.21"
    id("com.gradleup.shadow") version "8.3.6"
    id("dev.s7a.gradle.minecraft.server") version "3.2.1"
    id("com.google.devtools.ksp") version "2.1.21-2.0.1"
}

val pluginVersion = "2.0.0"

repositories {
    mavenCentral()
    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.1.21")
    implementation("io.insert-koin:koin-core:4.1.0-RC1")
    api("io.insert-koin:koin-annotations:2.0.1-RC1")
    ksp("io.insert-koin:koin-ksp-compiler:2.0.1-RC1")
}

group = "xyz.atrius"
version = pluginVersion
description = "Waystones"

tasks.shadowJar {
    minimize()
    archiveClassifier.set("")
    archiveVersion.set(pluginVersion)
}

tasks.build {
    delete(
        "build/MinecraftServer/plugins/waystones",
        "build/MinecraftServer/plugins/${project.name}-${project.version}.jar",
        "build/libs"
    )
}

task("buildPlugin") {
    dependsOn("shadowJar")

    doFirst {
        copy {
            from(rootDir.resolve("build/libs"))
                .include("${project.name}-${project.version}.jar")
            into(rootDir.resolve("build/MinecraftServer/plugins"))
        }
    }
}

task<LaunchMinecraftServerTask>("testPlugin") {
    dependsOn("buildPlugin")

    jarUrl.set(LaunchMinecraftServerTask.JarUrl.Paper("1.21.4"))
    agreeEula.set(true)
}