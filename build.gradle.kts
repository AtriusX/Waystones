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

dependencies {
    compileOnly("io.papermc.paper:paper-api:$buildPaperVersion.build.+")
    implementation(libs.kotlin.stdlib)
    implementation(libs.koin.core)
    implementation(libs.koin.annotations)
    implementation(libs.bstats.bukkit)
    implementation(libs.arrow.core)
    implementation(libs.flyway.core)
    implementation(libs.flyway.mysql)
    implementation(libs.sqlite.jdbc)
    implementation(libs.mysql.connector)
    detektPlugins(libs.detekt.ktlint)
    testImplementation(libs.mockk)
    testImplementation(libs.kotest.runner)
    testImplementation(libs.mockbukkit)
    testImplementation("io.papermc.paper:paper-api:$buildPaperVersion.build.+")
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
