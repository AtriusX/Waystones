package xyz.atrius.waystones.data.json

import com.google.gson.FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES
import com.google.gson.Gson
import com.google.gson.GsonBuilder

interface Json<T> {

    fun toJson(): String =
        GsonBuilder()
            .setFieldNamingPolicy(LOWER_CASE_WITH_UNDERSCORES)
            .setPrettyPrinting()
            .create().toJson(this)

    companion object {
        inline fun <reified  T : Json<T>> parse(input: String): T =
            Gson().fromJson(input, T::class.java)
    }
}