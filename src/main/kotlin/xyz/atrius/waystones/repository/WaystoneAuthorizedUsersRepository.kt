package xyz.atrius.waystones.repository

import org.flywaydb.core.internal.jdbc.RowMapper
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single
import xyz.atrius.waystones.config.DatabaseProperties
import xyz.atrius.waystones.config.SupportedDatabase
import xyz.atrius.waystones.dao.WaystoneAuthorizedUser
import xyz.atrius.waystones.manager.DatabaseManager
import java.sql.ResultSet
import java.util.UUID
import java.util.concurrent.CompletableFuture

@Single
class WaystoneAuthorizedUsersRepository(
    private val databaseManager: DatabaseManager,
    @Provided private val databaseProperties: DatabaseProperties,
) : RowMapper<WaystoneAuthorizedUser> {

    fun isAuthorized(waystoneUuid: UUID, playerUuid: UUID): CompletableFuture<Boolean> {
        val query = """
            |select 1
            |from waystone_authorized_users
            |where waystone_uuid = ?
            |  and owner_uuid = ?
            |limit 1;
        """.trimMargin()
        val params = listOf(waystoneUuid.toString(), playerUuid.toString())
        return databaseManager.queryExists(query, params)
    }

    fun addAuthorized(waystoneUuid: UUID, playerUuid: UUID): CompletableFuture<Boolean> {
        val query = when (databaseProperties.type) {
            SupportedDatabase.MYSQL -> """
                |insert ignore into waystone_authorized_users (waystone_uuid, owner_uuid)
                |values (?, ?);
            """.trimMargin()
            SupportedDatabase.SQLITE -> """
                |insert or ignore into waystone_authorized_users (waystone_uuid, owner_uuid)
                |values (?, ?);
            """.trimMargin()
        }
        val params = listOf(waystoneUuid.toString(), playerUuid.toString())
        return databaseManager
            .queryUpdate(query, params)
            .thenApplyAsync { it > 0 }
    }

    fun removeAuthorized(waystoneUuid: UUID, playerUuid: UUID): CompletableFuture<Boolean> {
        val query = """
            |delete from waystone_authorized_users
            |where waystone_uuid = ?
            |  and owner_uuid = ?;
        """.trimMargin()
        val params = listOf(waystoneUuid.toString(), playerUuid.toString())
        return databaseManager
            .queryUpdate(query, params)
            .thenApplyAsync { it > 0 }
    }

    fun getAuthorized(waystoneUuid: UUID): CompletableFuture<List<WaystoneAuthorizedUser>> {
        val query = """
            |select waystone_uuid, owner_uuid
            |from waystone_authorized_users
            |where waystone_uuid = ?;
        """.trimMargin()
        val params = listOf(waystoneUuid.toString())
        return databaseManager
            .queryAll(query, params, this)
    }

    override fun mapRow(rs: ResultSet): WaystoneAuthorizedUser = WaystoneAuthorizedUser(
        waystoneUuid = UUID.fromString(rs.getString("waystone_uuid")),
        ownerUuid = UUID.fromString(rs.getString("owner_uuid")),
    )
}
