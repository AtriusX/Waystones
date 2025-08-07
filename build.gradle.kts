import dev.s7a.gradle.minecraft.server.tasks.LaunchMinecraftServerTask
import io.papermc.hangarpublishplugin.internal.util.capitalized

plugins {
    id("java")
    id("idea")
    id("org.jetbrains.kotlin.jvm") version "2.2.0"
    id("com.gradleup.shadow") version "8.3.6"
    id("dev.s7a.gradle.minecraft.server") version "3.2.1"
    id("com.google.devtools.ksp") version "2.2.0-2.0.2"
    id("io.gitlab.arturbosch.detekt") version "1.23.8"
    id("io.papermc.hangar-publish-plugin") version "0.1.3"
    id("com.modrinth.minotaur") version "2.8.7"
}

val buildPaperVersion: String by project
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
    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.2.0")
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

    relocate("kotlin", "xyz.atrius.waystones.kotlin")

    dependencies {
        exclude(dependency("io.insert-koin:koin-core"))
        exclude(dependency("io.arrow-kt:arrow-core"))
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

fun buildProviders(vararg properties: String): Map<String, Provider<Any>> = properties
    .associateWith { providers.provider { project.properties[it] } }

tasks.processResources {
    val providers = buildProviders(
        "version",
        "buildPaperVersion",
        "pluginApiVersion",
        "paperVersions",
        "pluginWebsite",
    )

    doLast {
        filesMatching("paper-plugin.yml") {
            expand(providers.mapValues { it.value.get() })
        }
    }
}

tasks.build {
    delete(
        "build/MinecraftServer/plugins/waystones",
        "build/MinecraftServer/plugins/$outputProjectName.jar",
        "build/libs"
    )
}

tasks.register("buildPlugin") {
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

tasks.register<LaunchMinecraftServerTask>("testPlugin") {
    dependsOn("buildPlugin")

    jarUrl.set(LaunchMinecraftServerTask.JarUrl.Paper(buildPaperVersion))
    agreeEula.set(true)
}

val gitHash: String by lazy {
    providers
        .exec { commandLine("git", "rev-parse", "--short", "HEAD") }
        .standardOutput
        .asText
        .map { it.trim() }
        .get()
}
val supported = paperVersions
    .split(",")
    .map { it.trim() }

hangarPublish {
    publications.register("WaystonesRelease") {
        version = pluginVersion
        id = "waystones"
        channel = "Release"
        changelog = file("CHANGELOG.md").readText()
        apiKey = System.getenv("HANGAR_API_TOKEN")

        platforms {
            paper {
                jar = tasks.shadowJar.flatMap { it.archiveFile }
                platformVersions = supported
            }
        }
    }

    publications.register("WaystonesSnapshot") {
        version = "$pluginVersion-SNAPSHOT+$gitHash"
        id = "waystones"
        channel = "Snapshot"
        changelog = "${project.name.capitalized()} Dev Snapshot [$gitHash]"
        apiKey = System.getenv("HANGAR_API_TOKEN")

        platforms {
            paper {
                jar = tasks.shadowJar.flatMap { it.archiveFile }
                platformVersions = supported
            }
        }
    }
}

modrinth {
    val channel = System
        .getenv("MODRINTH_PUBLISH_CHANNEL")
        ?: "alpha"

    when (channel) {
        "release" -> {
            versionNumber = pluginVersion
            changelog = file("CHANGELOG.md").readText()
        }

        else -> {
            versionNumber = "$pluginVersion-SNAPSHOT+$gitHash"
            changelog = "${project.name.capitalized()} Dev Snapshot [$gitHash]"
        }
    }
    // Common values
    token = System.getenv("MODRINTH_TOKEN")
    projectId = "atri-waystones"
    versionName = "${project.name.capitalized()} $pluginVersion"
    uploadFile.set(tasks.shadowJar)
    gameVersions = supported
    loaders = listOf("paper")
    versionType = channel
}