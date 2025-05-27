package xyz.atrius.waystones.data.json

import com.google.gson.FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.bukkit.Material
import xyz.atrius.waystones.data.json.serializer.MaterialTypeAdapter

interface Json<T> {

    fun toJson(): String =
        GsonBuilder()
            .setFieldNamingPolicy(LOWER_CASE_WITH_UNDERSCORES)
            .registerTypeAdapter(Material::class.java, MaterialTypeAdapter)
            .setPrettyPrinting()
            .create().toJson(this)

    companion object {
        inline fun <reified  T : Json<T>> parse(input: String): T =
            Gson().fromJson(input, T::class.java)
    }
}