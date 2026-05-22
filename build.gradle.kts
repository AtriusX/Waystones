plugins {
    id("waystones.base")
    id("waystones.build-config")
    id("waystones.detekt")
    id("waystones.shadow")
    id("waystones.flyway")
    id("waystones.minecraft-server")
    id("waystones.hangar-publish")
    id("waystones.modrinth-publish")
    id("waystones.resource-processing")
}

configurations {
    testImplementation {
        extendsFrom(compileOnly)
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:$buildPaperVersion.build.+")
    implementation(libs.kotlin.stdlib)
    implementation(libs.koin.core)
    implementation(libs.koin.annotations)
    implementation(libs.bstats.bukkit)
    // Resolved at runtime via PluginLoader - not bundled in shadow JAR
    compileOnly(libs.arrow.core)
    compileOnly(libs.flyway.core)
    compileOnly(libs.flyway.mysql)
    compileOnly(libs.sqlite.jdbc)
    compileOnly(libs.mysql.connector)
    detektPlugins(libs.detekt.ktlint)
    testImplementation(libs.mockk)
    testImplementation(libs.kotest.runner)
    testImplementation(libs.kotest.datatest)
    testImplementation(libs.mockbukkit)
}

tasks.withType<Test> {
    useJUnitPlatform()
    systemProperty("bstats.relocatecheck", "false")
    systemProperty("kotest.framework.classpath.scanning.autoscan.disable", "true")
    systemProperty("kotest.framework.coroutine.test.scope", "false")
    testLogging {
        events("passed", "skipped", "failed")
        showStandardStreams = true
    }
}
