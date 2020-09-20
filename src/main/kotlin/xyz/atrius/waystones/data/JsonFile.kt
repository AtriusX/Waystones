package xyz.atrius.waystones.data

import com.google.gson.GsonBuilder
import xyz.atrius.waystones.utility.KotlinPlugin
import java.io.File

open class JsonFile(private val plugin: KotlinPlugin, name: String) {
    internal val file = File(plugin.dataFolder, "$name.json")
    internal val json = GsonBuilder().setPrettyPrinting().create()

    init {
        load()
    }

    private fun load() {
        try {
            if (!file.exists()) plugin.saveResource(file.name, false)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}