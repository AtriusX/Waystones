package xyz.atrius.waystones.service

import org.bukkit.Location
import xyz.atrius.waystones.utility.KotlinPlugin
import xyz.atrius.waystones.data.JsonFile
import xyz.atrius.waystones.utility.locationCode
import java.io.FileReader
import java.nio.file.Files
import java.nio.file.StandardOpenOption

class WarpNameService(plugin: KotlinPlugin) : JsonFile(plugin, "warpnames") {
    private val names: HashMap<String, Any> = json.fromJson(FileReader(file), HashMap<String, Any>().javaClass)

    fun add(location: Location, name: String) {
        names[location.locationCode] = name
        saveFile()
    }

    fun remove(location: Location?) {
        names.remove(location?.locationCode)
        saveFile()
    }

    private fun saveFile() {
        val json = json.toJson(names)
        file.delete()
        Files.write(file.toPath(), json.toByteArray(), StandardOpenOption.CREATE, StandardOpenOption.WRITE)
    }

    operator fun get(location: Location?) =
        names[location?.locationCode]
}