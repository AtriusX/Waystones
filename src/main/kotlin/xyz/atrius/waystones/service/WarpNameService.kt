package xyz.atrius.waystones.service

import com.google.gson.Gson
import org.bukkit.Bukkit
import org.koin.core.annotation.Single
import org.slf4j.LoggerFactory
import xyz.atrius.waystones.dao.WaystoneInfo
import xyz.atrius.waystones.data.JsonFile
import xyz.atrius.waystones.internal.KotlinPlugin
import xyz.atrius.waystones.repository.WaystoneInfoRepository
import xyz.atrius.waystones.utility.expectSizeOrDefault

@Single
@Deprecated("Database support is included now, so this will be phased out in the future")
class WarpNameService(
    gson: Gson,
    plugin: KotlinPlugin,
    private val waystoneInfoRepository: WaystoneInfoRepository,
    private val waystoneService: WaystoneService,
) : JsonFile<HashMap<String, String>>("warpnames", gson, plugin, HashMap<String, String>()::class) {

    fun migrate(): Int {
        logger.info("Migrating waystone names to database storage...")

        var failedMigrations = 0
        // Migrate old data to database format
        for ((locationCode, waystoneName) in data) {
            // Ensure our location code provides world and coordinate components in the proper format
            val components = locationCode
                .split("@")
                .expectSizeOrDefault(2) {
                    logger.warn("Invalid data in components for waystone '$waystoneName': $it")
                    logger.warn("Please be sure your waystone code is marked in the format 'worldName@x:y:z'!")
                    failedMigrations++
                    continue
                }
            val (worldName, location) = components
            val world = Bukkit
                .getWorld(worldName)
            // Ensure the waystone is associated to a valid world
            if (world == null) {
                logger.warn("Invalid world provided for waystone '$waystoneName': $worldName")
                failedMigrations++
                continue
            }
            // Ensure the entry has a valid world coordinate set
            val coordinates = location
                .split(":")
                .mapNotNull { it.toIntOrNull() }
                .expectSizeOrDefault(3) {
                    logger.warn("Unable to determine location for waystone '$waystoneName': $it")
                    logger.warn("Please be sure your x/y/z components are valid numbers!'")
                    failedMigrations++
                    continue
                }
            val (x, y, z) = coordinates
            // Ensure the data present is actually associated to a waystone block
            if (!waystoneService.isWaystone(world.getBlockAt(x, y, z))) {
                logger.warn("Block in world '${world.name}' @ ($x, $y, $z) is not a waystone. Skipping...")
                failedMigrations++
                continue
            }
            // Create info object and persist it to the database
            val info = WaystoneInfo(
                worldUid = world.uid,
                x = x,
                y = y,
                z = z,
                name = waystoneName,
            )
            // Save old data to new format
            waystoneInfoRepository.save(info)
        }

        logger.info("Waystone info migration complete!")
        return failedMigrations
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
