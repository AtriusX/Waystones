package xyz.atrius.waystones.repository

import org.bukkit.Location
import org.bukkit.World
import org.flywaydb.core.internal.jdbc.RowMapper
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single
import xyz.atrius.waystones.config.DatabaseProperties
import xyz.atrius.waystones.config.SupportedDatabase
import xyz.atrius.waystones.dao.WaystoneInfo
import xyz.atrius.waystones.manager.DatabaseManager
import java.sql.ResultSet
import java.util.UUID
import java.util.concurrent.CompletableFuture

@Single
class WaystoneInfoRepository(
    private val databaseManager: DatabaseManager,
    @Provided private val databaseProperties: DatabaseProperties,
) : RowMapper<WaystoneInfo> {

    fun getWaystone(location: Location): CompletableFuture<WaystoneInfo?> {
        val query = """
            |select *
            |from waystone_info
            |where world_uid = ?
            |  and x = ?
            |  and y = ?
            |  and z = ?;
        """.trimMargin()
        val params = listOf(location.world.uid, location.x, location.y, location.z)

        return databaseManager
            .query(query, params, this)
    }

    fun getAll(limit: Int, offset: Int): CompletableFuture<List<WaystoneInfo>> {
        val query = """
            |select *
            |from waystone_info
            |limit ? offset ?;
        """.trimMargin()
        val params = listOf(limit, offset)

        return databaseManager
            .queryAll(query, params, this)
    }

    fun entries(): CompletableFuture<Int> {
        val query = """
            |select count(*)
            |from waystone_info;
        """.trimMargin()

        return databaseManager
            .query(query, rowMapper = CountRowMapper)
            .thenApplyAsync { it ?: 0 }
    }

    fun existsByLocation(location: Location): CompletableFuture<Boolean> {
        val query = """
            |select 1
            |from waystone_info
            |where world_uid = ?
            |  and x = ?
            |  and y = ?
            |  and z = ?
            |limit 1;
        """.trimMargin()
        val params = listOf(location.world.uid, location.x, location.y, location.z)

        return databaseManager
            .queryExists(query, params)
    }

    fun save(info: WaystoneInfo): CompletableFuture<Int> {
        val conflictClause = when (databaseProperties.type) {
            SupportedDatabase.SQLITE -> """
                |on conflict (world_uid, x, y, z) do update set
                |   name = excluded.name,
                |   primary_owner_uuid = excluded.primary_owner_uuid,
                |   is_locked = excluded.is_locked
            """.trimMargin()
            SupportedDatabase.MYSQL -> """
                |on duplicate key update
                |   name = values(name),
                |   primary_owner_uuid = values(primary_owner_uuid),
                |   is_locked = values(is_locked)
            """.trimMargin()
        }
        val query = """
            |insert into waystone_info (world_uid, x, y, z, name, waystone_uuid, primary_owner_uuid, is_locked)
            |values (?, ?, ?, ?, ?, ?, ?, ?)
            |$conflictClause
        """.trimMargin()
        val params = listOf(
            info.worldUid, info.x, info.y, info.z, info.name,
            info.waystoneUuid?.toString(), info.primaryOwnerUuid?.toString(), info.isLocked,
        )

        return databaseManager
            .queryUpdate(query, params)
    }

    fun deleteByWorld(world: World): CompletableFuture<Int> {
        val query = """
            |delete from waystone_info 
            |where world_uid = ?;
        """.trimMargin()
        val params = listOf(world.uid)

        return databaseManager
            .queryUpdate(query, params)
    }

    fun deleteByLocation(location: Location): CompletableFuture<Int> {
        val query = """
            |delete from waystone_info 
            |where world_uid = ?
            |  and x = ?
            |  and y = ?
            |  and z = ?;
        """.trimMargin()
        val params = listOf(location.world.uid, location.x, location.y, location.z)

        return databaseManager
            .queryUpdate(query, params)
    }

    fun getLockedCount(): CompletableFuture<Int> {
        val query = """
            |select count(*)
            |from waystone_info
            |where is_locked = true;
        """.trimMargin()
        return databaseManager
            .query(query, rowMapper = CountRowMapper)
            .thenApplyAsync { it ?: 0 }
    }

    override fun mapRow(rs: ResultSet): WaystoneInfo = WaystoneInfo(
        worldUid = UUID.fromString(rs.getString("world_uid")),
        x = rs.getInt("x"),
        y = rs.getInt("y"),
        z = rs.getInt("z"),
        name = rs.getString("name"),
        waystoneUuid = rs.getString("waystone_uuid")?.let(UUID::fromString),
        primaryOwnerUuid = rs.getString("primary_owner_uuid")?.let(UUID::fromString),
        isLocked = rs.getBoolean("is_locked"),
    )
}
