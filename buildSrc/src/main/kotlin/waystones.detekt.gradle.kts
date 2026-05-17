plugins {
    id("waystones.base")
    id("dev.detekt")
}

dependencies {
    detektPlugins("dev.detekt:detekt-rules-ktlint-wrapper:2.0.0-alpha.2")
}

detekt {
    autoCorrect = true
    buildUponDefaultConfig = true
    config.setFrom("$projectDir/config/detekt.yml")
}
