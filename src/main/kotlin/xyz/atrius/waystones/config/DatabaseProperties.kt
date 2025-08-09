package xyz.atrius.waystones.config

import xyz.atrius.waystones.internal.KotlinPlugin

enum class SupportedDatabase(private val type: String) {
    SQLITE("sqlite"),
    MYSQL("mysql");

    override fun toString(): String = type
}

data class DatabaseProperties(
    val type: SupportedDatabase,
    val host: String? = null,
    val port: Int? = null,
    val username: String? = null,
    val password: String? = null,
    val databaseName: String? = null,
) {

    fun getHostUrl(plugin: KotlinPlugin): String {
        // We should configure flyway to pull migrations from the correct database directory
        val url = when (type) {
            SupportedDatabase.SQLITE -> plugin.dataFolder.resolve("waystones.db")
            else -> "//$host:$port/$databaseName"
        }

        return "jdbc:$type:$url"
    }
}
