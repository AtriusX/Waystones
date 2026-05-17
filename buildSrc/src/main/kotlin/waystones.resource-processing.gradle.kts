plugins {
    id("waystones.base")
}

tasks.processResources {
    val version = providers.gradleProperty("version")
    val buildPaperVersionProp = providers.gradleProperty("buildPaperVersion")
    val pluginApiVersion = providers.gradleProperty("pluginApiVersion")
    val paperVersionsProp = providers.gradleProperty("paperVersions")
    val pluginWebsite = providers.gradleProperty("pluginWebsite")

    filesMatching("paper-plugin.yml") {
        expand(
            "version" to version.get(),
            "buildPaperVersion" to buildPaperVersionProp.get(),
            "pluginApiVersion" to pluginApiVersion.get(),
            "paperVersions" to paperVersionsProp.get(),
            "pluginWebsite" to pluginWebsite.get(),
        )
    }
}
