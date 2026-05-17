plugins {
    id("waystones.base")
}

val versionProp = providers.gradleProperty("version")
val buildPaperVersionProp = providers.gradleProperty("buildPaperVersion")
val pluginApiVersionProp = providers.gradleProperty("pluginApiVersion")
val paperVersionsProp = providers.gradleProperty("paperVersions")
val pluginWebsiteProp = providers.gradleProperty("pluginWebsite")

tasks.processResources {
    val version = versionProp.get()
    val buildPaperVersion = buildPaperVersionProp.get()
    val pluginApiVersion = pluginApiVersionProp.get()
    val paperVersions = paperVersionsProp.get()
    val pluginWebsite = pluginWebsiteProp.get()

    filesMatching("paper-plugin.yml") {
        expand(
            "version" to version,
            "buildPaperVersion" to buildPaperVersion,
            "pluginApiVersion" to pluginApiVersion,
            "paperVersions" to paperVersions,
            "pluginWebsite" to pluginWebsite,
        )
    }
}
