package xyz.atrius.waystones.data

import com.google.gson.GsonBuilder
import xyz.atrius.waystones.plugin
import java.io.File
import java.io.FileReader
import java.nio.file.Files
import kotlin.collections.MutableMap.MutableEntry

open class JsonFile<T>(name: String) : File(plugin.dataFolder, "$name.json") {
    private val json = GsonBuilder().setPrettyPrinting().create()
    protected lateinit var data: HashMap<String, T>

    fun load() {
        if (!exists()) runCatching {
            plugin.saveResource(name, false)
        }.onFailure {
            it.printStackTrace()
        }
        data = json.fromJson(FileReader(this), HashMap<String, T>()::class.java)
        onLoad()
    }

    open fun onLoad() {}

    fun save(forceLowercase: Boolean = false, action: () -> Unit = {}) {
        action()
        if (forceLowercase)
            data.mapKeysTo(data) { (key) -> key.lowercase() }
        val json = json.toJson(data)
        Files.write(toPath(), json.toByteArray())
        onSave()
    }

    open fun onSave() {}

    operator fun iterator(): Iterator<MutableEntry<String, T>> =
        data.iterator()
}
