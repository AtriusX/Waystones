package xyz.atrius.waystones.config

import org.bstats.bukkit.Metrics
import org.bstats.charts.SimplePie
import org.flywaydb.core.Flyway
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.slf4j.LoggerFactory
import xyz.atrius.waystones.internal.KotlinPlugin
import xyz.atrius.waystones.utility.bindAsEnum

@Module
object DatabaseModule {

    @Single
    fun getDatabaseConfiguration(
        plugin: KotlinPlugin,
        metrics: Metrics,
    ): DatabaseProperties {
        logger.info("Attempting to load database configuration...")

        val config = plugin.config
            .getConfigurationSection("database")
            ?: return defaultDatabaseProperties()
        val database = config
            .getString("type")
            .bindAsEnum<SupportedDatabase>()
        // Track database type usage
        metrics.addCustomChart(
            SimplePie("database_type") {
                database.description
            }
        )

        return DatabaseProperties(
            type = database,
            host = config.getString("host"),
            port = config.getInt("port"),
            username = config.getString("username"),
            password = config.getString("password"),
            databaseName = config.getString("database-name")
        )
    }

    private fun defaultDatabaseProperties(): DatabaseProperties =
        DatabaseProperties(SupportedDatabase.SQLITE)

    @Single
    fun configureFlyway(
        plugin: KotlinPlugin,
        props: DatabaseProperties,
    ): Flyway = Flyway
        .configure(this::class.java.classLoader)
        .validateOnMigrate(true)
        .locations("classpath:db/migration/${props.type}", "classpath:/db/migration/common")
        .dataSource(props.getHostUrl(plugin), props.username, props.password)
        .load()

    private val logger = LoggerFactory
        .getLogger(DatabaseModule::class.java)
}
