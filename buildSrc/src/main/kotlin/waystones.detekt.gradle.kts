import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    id("waystones.base")
    id("dev.detekt")
}

val libs = the<LibrariesForLibs>()

dependencies {
    detektPlugins(libs.detekt.ktlint)
}

detekt {
    autoCorrect = true
    buildUponDefaultConfig = true
    config.setFrom("$projectDir/config/detekt.yml")
}
