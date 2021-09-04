package xyz.atrius.waystones.data

import com.google.gson.GsonBuilder
import xyz.atrius.waystones.plugin
import java.io.File
import java.io.FileReader
import java.nio.file.Files

open class JsonFile<T>(name: String) {
    private val file = File(plugin.dataFolder, "$name.json")
    private val json = GsonBuilder().setPrettyPrinting().create()
    protected lateinit var data: HashMap<String, T>

    fun load() {
        try {
            if (!file.exists())
                plugin.saveResource(file.name, false)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        data = json.fromJson(FileReader(file), HashMap<String, T>()::class.java)
    }

    fun save(forceLowercase: Boolean = false) {
        if (forceLowercase)
            data.mapKeysTo(data) { (key) -> key.toLowerCase() }
        val json = json.toJson(data)
        Files.write(file.toPath(), json.toByteArray())
    }
}
