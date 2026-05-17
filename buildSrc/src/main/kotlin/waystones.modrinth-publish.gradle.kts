plugins {
    id("waystones.base")
    id("waystones.shadow")
    id("com.modrinth.minotaur")
}

val channel = System.getenv("MODRINTH_PUBLISH_CHANNEL") ?: "alpha"

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
            changelog = "${project.name.capitalized()} Dev Snapshot [$gitHash.get()]"
        }
    }
    token = System.getenv("MODRINTH_TOKEN")
    projectId = "atri-waystones"
    versionName = "${project.name.capitalized()} $pluginVersion"
    uploadFile.set(tasks.shadowJar)
    gameVersions = supportedVersions
    loaders = listOf("paper")
    versionType = channel
}

tasks.modrinth {
    notCompatibleWithConfigurationCache("Do not cache artifacts")
}
