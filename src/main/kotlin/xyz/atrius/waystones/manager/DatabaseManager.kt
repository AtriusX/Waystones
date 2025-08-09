package xyz.atrius.waystones.manager

import org.flywaydb.core.Flyway
import org.flywaydb.core.api.output.MigrateResult
import org.flywaydb.core.internal.jdbc.RowMapper
import org.koin.core.annotation.Single
import org.slf4j.LoggerFactory
import xyz.atrius.waystones.config.DatabaseProperties
import xyz.atrius.waystones.internal.KotlinPlugin
import java.sql.Connection
import java.sql.DriverManager
import java.util.concurrent.CompletableFuture

@Single
class DatabaseManager(
    private val flyway: Flyway,
    private val properties: DatabaseProperties,
    private val plugin: KotlinPlugin,
) : AutoCloseable {
    private var connection: Connection? = null

    fun load() {
        if (connection != null) {
            logger.error("Cannot reload DatabaseManager... Class already loaded!")
            return
        }
        // Perform database migrations, this is only done at plugin load to ensure consistency during server lifetime
        logger.info("Performing database migrations...")

        val result = flyway.migrate()
        // If migrations failed, log the failures in the console
        if (!result.success) {
            logFailedMigration(result)
            error("Migrations failed!")
        }

        val executions = result.migrationsExecuted

        when (executions) {
            0 -> logger.info("No new migrations performed!")
            else -> logger.info("Performed $executions migrations!")
        }

        logger.info("Creating connection to database...")
        connection = DriverManager.getConnection(properties.getHostUrl(plugin))
        logger.info("Connection generated!")
    }

    private fun logFailedMigration(result: MigrateResult) {
        val failedMigrations = result.failedMigrations
            .joinToString("\n") { "${it.version} - ${it.description}" }

        logger.error("Failed to perform database migrations! See below:")
        logger.error(failedMigrations)
    }

    fun <T> query(query: String, parameters: List<Any?>? = null, rowMapper: RowMapper<T>): CompletableFuture<T?> {
        val statement = connection
            ?.prepareStatement(query)
            ?: return CompletableFuture.failedFuture(IllegalStateException("Connection is not active!"))
        // Add parameters if any are provided
        parameters?.forEachIndexed { i, p ->
            statement.setObject(i + 1, p)
        }
        // Execute the query and map the row
        return CompletableFuture.supplyAsync {
            val result = statement
                .executeQuery()

            when (result.next()) {
                true -> rowMapper.mapRow(result)
                else -> null
            }
        }
    }

    fun queryExists(query: String, parameters: List<Any?>? = null): CompletableFuture<Boolean> {
        val statement = connection
            ?.prepareStatement(query)
            ?: return CompletableFuture.failedFuture(IllegalStateException("Connection is not active!"))
        // Add parameters if any are provided
        parameters?.forEachIndexed { i, p ->
            statement.setObject(i + 1, p)
        }

        return CompletableFuture.supplyAsync {
            statement.executeQuery().next()
        }
    }

    fun queryUpdate(query: String, parameters: List<Any?>? = null): CompletableFuture<Int> {
        val statement = connection
            ?.prepareStatement(query)
            ?: return CompletableFuture.failedFuture(IllegalStateException("Connection is not active!"))
        // Add parameters if any are provided
        return CompletableFuture.supplyAsync {
            parameters?.forEachIndexed { i, p ->
                statement.setObject(i + 1, p)
            }

            statement
                .executeUpdate()
        }
    }

    override fun close() {
        logger.info("Shutting down database connection...")
        connection?.close()
        connection = null
    }

    companion object {

        private val logger = LoggerFactory
            .getLogger(DatabaseManager::class.java)
    }
}
