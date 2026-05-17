import dev.s7a.gradle.minecraft.server.tasks.LaunchMinecraftServerTask

plugins {
    id("waystones.base")
    id("waystones.shadow")
    id("dev.s7a.gradle.minecraft.server")
}

val buildPaperVersion: String by project
val pluginVersion = "${project.version}-$buildPaperVersion"
val outputProjectName = "${project.name}-$pluginVersion"

tasks.build {
    delete(
        "build/MinecraftServer/plugins/waystones",
        "build/MinecraftServer/plugins/$outputProjectName.jar",
        "build/libs"
    )
}

tasks.register("buildPlugin") {
    notCompatibleWithConfigurationCache("Do not cache artifacts")
    dependsOn("shadowJar")

    doFirst {
        copy {
            from(rootDir.resolve("build/libs"))
                .include("$outputProjectName.jar")
            into(rootDir.resolve("build/MinecraftServer/plugins"))
        }
    }
}

tasks.register<LaunchMinecraftServerTask>("testPlugin") {
    notCompatibleWithConfigurationCache("Do not cache artifacts")
    dependsOn("buildPlugin")
    jarUrl.set(LaunchMinecraftServerTask.JarUrl.Paper(buildPaperVersion))
    agreeEula.set(true)
}
