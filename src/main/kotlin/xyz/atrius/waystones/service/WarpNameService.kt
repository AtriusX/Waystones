package xyz.atrius.waystones.service

import org.bukkit.Location
import xyz.atrius.waystones.data.JsonFile
import xyz.atrius.waystones.utility.locationCode
import java.io.FileReader
import java.nio.file.Files
import java.nio.file.StandardOpenOption.CREATE
import java.nio.file.StandardOpenOption.WRITE

object WarpNameService : JsonFile("warpnames") {
    private val names: HashMap<String, String> =
        json.fromJson(FileReader(file), HashMap<String, String>()::class.java)

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
        Files.write(file.toPath(), json.toByteArray(), CREATE, WRITE)
    }

    operator fun get(location: Location?) =
        names[location?.locationCode]
}