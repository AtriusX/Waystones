package xyz.atrius.waystones.service

import org.bukkit.Location
import xyz.atrius.waystones.data.JsonFile
import xyz.atrius.waystones.data.Node
import xyz.atrius.waystones.data.Waystone
import xyz.atrius.waystones.data.config.LocationParser
import xyz.atrius.waystones.log
import xyz.atrius.waystones.utility.toLocation

object WarpService : JsonFile<Node<*>>("warps") {

    /* This is temporary code that is used to migrate our existing data from
     * our legacy name service. Once migration is complete the service will be
     * deleted and will not be loaded on the next server restart.
     *
     * This migration feature should be removed in a later release.
     */
    @Suppress("DEPRECATION")
    override fun onLoad() {
        // Check if an existing name file exists
        if (WarpNameService.exists()) {
            log.info("Existing warpnames.json file detected! Attempting to migrate.")
            WarpNameService.load()
            for ((code, name) in WarpNameService) {
                val location = code.toLocation()
                if (location != null)
                    add(location, Waystone(name))
            }
            WarpNameService.delete()
            log.info("Migration of warpnames.json complete.")
        }
    }

    fun add(location: Location, waystone: Node<*>) = save {
        data[LocationParser.toString(location)] = waystone
    }

    fun remove(location: Location) = save {
        data.remove(LocationParser.toString(location))
    }

    operator fun get(location: Location?): Node<*>? {
        location ?: return null
        return data[LocationParser.toString(location)]
    }
}

