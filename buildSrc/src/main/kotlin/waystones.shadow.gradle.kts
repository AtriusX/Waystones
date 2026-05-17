plugins {
    id("waystones.base")
    id("com.gradleup.shadow")
}

val buildPaperVersion: String by project

val pluginVersion = "${project.version}-$buildPaperVersion"

tasks.shadowJar {
    minimize {
        exclude(dependency("org.flywaydb:flyway-mysql:.*"))
    }
    mergeServiceFiles()
    archiveClassifier.set("")
    archiveVersion.set(pluginVersion)
    val location = project.group.toString()
    relocate("kotlin", location)
    relocate("org.bstats", location)
}
