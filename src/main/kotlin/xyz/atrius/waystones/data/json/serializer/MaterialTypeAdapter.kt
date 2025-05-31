package xyz.atrius.waystones.data.json.serializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import org.bukkit.Material
import java.lang.reflect.Type

object MaterialTypeAdapter : JsonSerializer<Material>, JsonDeserializer<Material> {

    private val entries = Material.entries

    override fun serialize(
        src: Material,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement? = JsonPrimitive(src.key.asString().lowercase())

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Material? {
        val key = json.asString

        return entries.firstOrNull {
            it.key.asString().equals(key, true)
        }
    }
}
