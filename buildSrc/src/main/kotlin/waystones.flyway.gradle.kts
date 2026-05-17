plugins {
    id("waystones.base")
    id("org.flywaydb.flyway")
}

flyway {
    url = System.getenv("DB_URL")
    user = System.getenv("DB_USER")
    password = System.getenv("DB_USER_PASSWORD")
    locations = arrayOf(
        "filesystem:$projectDir/src/main/resources/db/migration/common/**",
        "filesystem:$projectDir/src/main/resources/db/migration/${System.getenv("DB_TYPE")}/**",
    )
    cleanDisabled = false
}

tasks.disableConfigurationCache("flywayClean", "flywayValidate", "flywayMigrate")
