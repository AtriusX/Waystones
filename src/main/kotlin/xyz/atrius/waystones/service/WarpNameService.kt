package xyz.atrius.waystones.service

import org.bukkit.Location
import xyz.atrius.waystones.data.JsonFile
import xyz.atrius.waystones.utility.locationCode

@Deprecated("Warp name service will be removed in a later update.")
object WarpNameService : JsonFile<String>("warpnames") {

    fun add(location: Location, name: String) {
        data[location.locationCode] = name
        save()
    }

    fun remove(location: Location?) {
        data.remove(location?.locationCode)
        save()
    }

    operator fun get(location: Location?) =
        data[location?.locationCode]
}