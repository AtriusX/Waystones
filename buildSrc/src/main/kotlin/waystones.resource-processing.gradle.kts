plugins {
    id("waystones.base")
}

tasks.processResources {
    filesMatching("paper-plugin.yml") {
        expand(
            "version" to providers.gradleProperty("version").get(),
            "buildPaperVersion" to providers.gradleProperty("buildPaperVersion").get(),
            "pluginApiVersion" to providers.gradleProperty("pluginApiVersion").get(),
            "paperVersions" to providers.gradleProperty("paperVersions").get(),
            "pluginWebsite" to providers.gradleProperty("pluginWebsite").get(),
        )
    }
}
