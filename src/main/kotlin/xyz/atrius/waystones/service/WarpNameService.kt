package xyz.atrius.waystones.service

import com.google.gson.Gson
import org.bukkit.Bukkit
import org.koin.core.annotation.Single
import org.slf4j.LoggerFactory
import xyz.atrius.waystones.dao.WaystoneInfo
import xyz.atrius.waystones.data.JsonFile
import xyz.atrius.waystones.internal.KotlinPlugin
import xyz.atrius.waystones.repository.WaystoneInfoRepository

@Single
@Deprecated("Database support is included now, so this will be phased out in the future")
class WarpNameService(
    gson: Gson,
    plugin: KotlinPlugin,
    private val waystoneInfoRepository: WaystoneInfoRepository
) : JsonFile<HashMap<String, String>>("warpnames", gson, plugin, HashMap<String, String>()::class) {

    fun migrate() {
        logger.info("Migrating waystone names to database storage...")

        // Migrate old data to database format
        for ((locationCode, waystoneName) in data) {
            val (worldName, location) = locationCode
                .split("@")
            val world = Bukkit
                .getWorld(worldName)
                ?: continue
            val coordinates = location
                .split(":")
                .map { it.toIntOrNull() }

            if (coordinates.size != 3) {
                continue
            }

            val (x, y, z) = coordinates
            val info = WaystoneInfo(
                worldUid = world.uid,
                x = x ?: continue,
                y = y ?: continue,
                z = z ?: continue,
                name = waystoneName,
            )
            // Save old data to new format
            waystoneInfoRepository.save(info)
        }

        logger.info("Waystone info migration complete!")
    }

    fun clearData() {
        // Delete old data
        data.clear()
        save()
    }

    companion object {
        private val logger = LoggerFactory
            .getLogger(WarpNameService::class.java)
    }
}
