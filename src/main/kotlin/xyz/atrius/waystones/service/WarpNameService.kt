package xyz.atrius.waystones.service

import com.google.gson.Gson
import org.bukkit.Location
import org.koin.core.annotation.Single
import xyz.atrius.waystones.data.JsonFile
import xyz.atrius.waystones.internal.KotlinPlugin
import xyz.atrius.waystones.utility.locationCode

@Single
class WarpNameService(
    gson: Gson,
    plugin: KotlinPlugin,
) : JsonFile<HashMap<String, String>>("warpnames", gson, plugin, HashMap<String, String>()::class) {

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
