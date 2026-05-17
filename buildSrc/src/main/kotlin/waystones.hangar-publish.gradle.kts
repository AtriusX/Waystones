import io.papermc.hangarpublishplugin.HangarPublishTask

plugins {
    id("waystones.base")
    id("waystones.shadow")
    id("io.papermc.hangar-publish-plugin")
}

fun String.capitalized(): String =
    replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }

fun extractChangelog(content: String): String {
    val lines = content.split("\n")
    var skip = true
    if (lines.size == 1) return content
    for (i in lines.indices) {
        if (!lines[i].startsWith("## ")) continue
        if (skip) { skip = false; continue }
        return lines.subList(0, i).joinToString("\n").trim()
    }
    return content
}

val buildPaperVersion: String by project
val paperVersions: String by project
val pluginVersion = "${project.version}-$buildPaperVersion"

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

tasks.withType<HangarPublishTask> {
    notCompatibleWithConfigurationCache("Do not cache artifacts")
}

hangarPublish {
    publications.register("WaystonesRelease") {
        version = pluginVersion
        id = "waystones"
        channel = "Release"
        changelog = file("CHANGELOG.md")
            .readText()
            .let(::extractChangelog)
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
