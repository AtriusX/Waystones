package xyz.atrius.waystones.data

import com.google.gson.Gson
import org.slf4j.LoggerFactory
import xyz.atrius.waystones.internal.KotlinPlugin
import java.io.File
import java.io.FileReader
import java.nio.file.Files
import kotlin.reflect.KClass

open class JsonFile<T : Any>(
    name: String,
    private val gson: Gson,
    private val plugin: KotlinPlugin,
    private val type: KClass<out T>,
) {

    private val file = File(plugin.dataFolder, "$name.json")
    protected lateinit var data: T

    open fun load() {
        logger.info("Loading ${file.name}...")

        try {
            if (!file.exists()) {
                plugin.saveResource(file.name, false)
            }
        } catch (e: IllegalArgumentException) {
            logger.error("Failed to load configuration file: ${e.message}")
        }

        data = gson.fromJson(FileReader(file), type.java)
        logger.info("Loaded ${file.name} successfully!")
    }

    fun save() {
        val json = gson.toJson(data)
        Files.write(file.toPath(), json.toByteArray())
    }

    companion object {

        private val logger = LoggerFactory
            .getLogger(JsonFile::class.java)
    }
}
