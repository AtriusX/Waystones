plugins {
    `kotlin-dsl`
}

fun DependencyHandlerScope.plugin(plugin: Provider<PluginDependency>) =
    plugin.map { "${it.pluginId}:${it.pluginId}.gradle.plugin:${it.version.requiredVersion}" }

dependencies {
    implementation(plugin(libs.plugins.kotlin.jvm))
    implementation(plugin(libs.plugins.koin.compiler))
    implementation(plugin(libs.plugins.shadow))
    implementation(plugin(libs.plugins.minecraft.server))
    implementation(plugin(libs.plugins.detekt))
    implementation(plugin(libs.plugins.hangar))
    implementation(plugin(libs.plugins.modrinth))
    implementation(plugin(libs.plugins.flyway))
    implementation(plugin(libs.plugins.buildconfig))
    implementation(libs.flyway.mysql)

    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}
