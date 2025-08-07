package xyz.atrius.waystones.repository

import org.bukkit.Location
import org.bukkit.World
import org.flywaydb.core.internal.jdbc.RowMapper
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
    private val databaseProperties: DatabaseProperties,
) : RowMapper<WaystoneInfo> {

    fun getWaystone(location: Location): CompletableFuture<WaystoneInfo?> {
        val query = """
            |select *
            |from waystone_info
            |where worldUid = ?
            |  and x = ?
            |  and y = ?
            |  and z = ?;
        """.trimMargin()
        val params = listOf(location.world.uid, location.x, location.y, location.z)

        return databaseManager
            .query(query, params, this)
    }

    fun save(info: WaystoneInfo): CompletableFuture<Int> {
        val query = when (databaseProperties.type) {
            SupportedDatabase.MYSQL -> """
                |insert into waystone_info (world_uid, x, y, z, name)
                |values (?, ?, ?, ?, ?)
                |on duplicate key update
                |   name = values(name)
            """.trimMargin()
            SupportedDatabase.SQLITE -> """
                |insert into waystone_info (world_uid, x, y, z, name)
                |values (?, ?, ?, ?, ?)
                |on conflict(world_uid, x, y, z) do update set
                |   name = excluded.name
            """.trimMargin()
        }
        val params = listOf(info.worldUid, info.x, info.y, info.z, info.name)

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

    override fun mapRow(rs: ResultSet): WaystoneInfo = WaystoneInfo(
        worldUid = UUID.fromString(rs.getString("world_uid")),
        x = rs.getInt("x"),
        y = rs.getInt("y"),
        z = rs.getInt("z"),
        name = rs.getString("name"),
    )
}
