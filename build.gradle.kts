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
    compileOnly("io.papermc.paper:paper-api:${project.properties["buildPaperVersion"]}.build.+")
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
}
