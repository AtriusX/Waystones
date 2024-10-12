package xyz.atrius.waystones.data.json

import com.google.gson.FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.bukkit.Bukkit
import org.bukkit.inventory.ItemStack

@Suppress("DEPRECATION")
interface Json<T> {

    fun toJson(): String =
        GsonBuilder()
            .setFieldNamingPolicy(LOWER_CASE_WITH_UNDERSCORES)
            .registerTypeHierarchyAdapter(ItemStack::class.java, object : TypeAdapter<ItemStack>() {
                private val jsonAdapter = Gson().getAdapter(JsonElement::class.java)

                override fun write(out: JsonWriter, value: ItemStack?) {
                    if (value == null || value.isEmpty) {
                        out.nullValue()
                    } else {
                        jsonAdapter.write(out, Bukkit.getUnsafe().serializeItemAsJson(value))
                    }
                }

                override fun read(`in`: JsonReader): ItemStack {
                    val value = jsonAdapter.read(`in`)
                    return if (value.isJsonNull) {
                        ItemStack.empty()
                    } else {
                        Bukkit.getUnsafe().deserializeItemFromJson(value.asJsonObject)
                    }
                }

            })
            .setPrettyPrinting()
            .create().toJson(this)

    companion object {
        inline fun <reified  T : Json<T>> parse(input: String): T =
            Gson().fromJson(input, T::class.java)
    }
}