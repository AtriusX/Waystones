plugins {
    java
    idea
    id("org.jetbrains.kotlin.jvm")
    id("io.insert-koin.compiler.plugin")
}

group = "xyz.atrius"
description = "Waystones"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
}

kotlin {
    jvmToolchain(25)
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}
