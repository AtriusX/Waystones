plugins {
    id("waystones.base")
    id("waystones.shadow")
    id("com.modrinth.minotaur")
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

val channel = System
    .getenv("MODRINTH_PUBLISH_CHANNEL")
    ?: "alpha"

modrinth {
    when (channel) {
        "release" -> {
            versionNumber = pluginVersion
            changelog = file("CHANGELOG.md")
                .readText()
                .let(::extractChangelog)
        }
        else -> {
            versionNumber = "$pluginVersion-SNAPSHOT+$gitHash"
            changelog = "${project.name.capitalized()} Dev Snapshot [$gitHash]"
        }
    }
    token = System.getenv("MODRINTH_TOKEN")
    projectId = "atri-waystones"
    versionName = "${project.name.capitalized()} $pluginVersion"
    uploadFile.set(tasks.shadowJar)
    gameVersions = supported
    loaders = listOf("paper")
    versionType = channel
}

tasks.modrinth {
    notCompatibleWithConfigurationCache("Do not cache artifacts")
}
