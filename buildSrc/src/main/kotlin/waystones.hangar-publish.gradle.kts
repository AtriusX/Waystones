import io.papermc.hangarpublishplugin.HangarPublishTask

plugins {
    id("waystones.base")
    id("waystones.shadow")
    id("io.papermc.hangar-publish-plugin")
}

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
                platformVersions = supportedVersions
            }
        }
    }

    publications.register("WaystonesSnapshot") {
        version = "$pluginVersion-SNAPSHOT+$gitHash"
        id = "waystones"
        channel = "Snapshot"
        changelog = "${project.name.capitalized()} Dev Snapshot [$gitHash.get()]"
        apiKey = System.getenv("HANGAR_API_TOKEN")

        platforms {
            paper {
                jar = tasks.shadowJar.flatMap { it.archiveFile }
                platformVersions = supportedVersions
            }
        }
    }
}
