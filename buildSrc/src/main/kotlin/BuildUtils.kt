import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.TaskContainer

fun String.capitalized(): String = replaceFirstChar {
    when (it.isLowerCase()) {
        true -> it.titlecase()
        else -> it.toString()
    }
}

fun extractChangelog(content: String): String {
    val lines = content.split("\n")
    var skip = true

    if (lines.size == 1) {
        return content
    }

    for (i in lines.indices) {
        if (!lines[i].startsWith("## ")) {
            continue
        }

        if (skip) {
            skip = false;
            continue
        }

        return lines
            .subList(0, i)
            .joinToString("\n")
            .trim()
    }

    return content
}

val Project.buildPaperVersion: String
    get() = project.properties["buildPaperVersion"] as String

val Project.paperVersions: String
    get() = project.properties["paperVersions"] as String

val Project.pluginVersion: String
    get() = "${project.version}-$buildPaperVersion"

val Project.outputProjectName: String
    get() = "${project.name}-$pluginVersion"

val Project.supportedVersions: List<String>
    get() = paperVersions
        .split(",")
        .map { it.trim() }

val Project.gitHash: Provider<String>
    get() = providers
        .exec { commandLine("git", "rev-parse", "--short", "HEAD") }
        .standardOutput
        .asText
        .map { it.trim() }

fun TaskContainer.disableConfigurationCache(vararg taskNames: String) {
    taskNames.forEach { name ->
        named(name) {
            notCompatibleWithConfigurationCache("Plugin does not handle configuration cache well")
        }
    }
}
