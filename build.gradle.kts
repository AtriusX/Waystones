import dev.s7a.gradle.minecraft.server.tasks.LaunchMinecraftServerTask

plugins {
    id("java")
    id("idea")
    id("org.jetbrains.kotlin.jvm") version "2.1.21"
    id("com.gradleup.shadow") version "8.3.6"
    id("dev.s7a.gradle.minecraft.server") version "3.2.1"
    id("com.google.devtools.ksp") version "2.1.21-2.0.1"
    id("io.gitlab.arturbosch.detekt") version "1.23.8"
    id("io.papermc.hangar-publish-plugin") version "0.1.3"
}

val buildPaperVersion: String by project
val pluginApiVersion: String by project
val paperVersions: String by project

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
    compileOnly("io.papermc.paper:paper-api:$buildPaperVersion-R0.1-SNAPSHOT")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.1.21")
    implementation("io.insert-koin:koin-core:4.1.0-RC1")
    implementation("io.arrow-kt:arrow-core:2.1.2")

    api("io.insert-koin:koin-annotations:2.0.1-RC1")
    ksp("io.insert-koin:koin-ksp-compiler:2.0.1-RC1")

    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.8")

    testImplementation("io.mockk:mockk:1.14.2")
    testImplementation("io.kotest:kotest-runner-junit5:5.9.1")
}

group = "xyz.atrius"
description = "Waystones"

val pluginVersion = "$version-$buildPaperVersion"
val outputProjectName = "${project.name}-$pluginVersion"

tasks.shadowJar {
    minimize()
    archiveClassifier.set("")
    archiveVersion.set(pluginVersion)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

tasks.processResources {
    filesMatching("paper-plugin.yml") {
        expand(
            "version" to version,
            "apiVersion" to pluginApiVersion,
        )
    }
}

tasks.build {
    delete(
        "build/MinecraftServer/plugins/waystones",
        "build/MinecraftServer/plugins/$outputProjectName.jar",
        "build/libs"
    )
}

task("buildPlugin") {
    dependsOn("shadowJar")

    doFirst {
        copy {
            from(rootDir.resolve("build/libs"))
                .include("$outputProjectName.jar")
            into(rootDir.resolve("build/MinecraftServer/plugins"))
        }
    }
}

detekt {
    autoCorrect = true
    buildUponDefaultConfig = true
    config.setFrom("$projectDir/config/detekt.yml")
}

task<LaunchMinecraftServerTask>("testPlugin") {
    dependsOn("buildPlugin")

    jarUrl.set(LaunchMinecraftServerTask.JarUrl.Paper(buildPaperVersion))
    agreeEula.set(true)
}

hangarPublish {
    val supported = paperVersions
        .split(",")
        .map { it.trim() }

    publications.register("WaystonesRelease") {
        version = pluginVersion
        id = "waystones"
        channel = "Release"
        apiKey = System.getenv("HANGAR_API_TOKEN")

        platforms {
            paper {
                jar = tasks.shadowJar.flatMap { it.archiveFile }
                platformVersions = supported
            }
        }
    }

    publications.register("WaystonesSnapshot") {
        val gitHash = providers
            .exec { commandLine("git", "rev-parse", "--short", "HEAD") }
            .standardOutput
            .asText
            .map { it.trim() }
            .orElse("")

        version = "$pluginVersion-SNAPSHOT+$gitHash"
        id = "waystones"
        channel = "Snapshot"
        apiKey = System.getenv("HANGAR_API_TOKEN")

        platforms {
            paper {
                jar = tasks.shadowJar.flatMap { it.archiveFile }
                platformVersions = supported
            }
        }
    }
}