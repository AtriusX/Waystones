package xyz.atrius.waystones.data.json

import com.google.gson.Gson

interface Json<T> {

    fun toJson(): String

    companion object {
        inline fun <reified  T : Json<T>> parse(input: String): T =
            Gson().fromJson(input, T::class.java)
    }
}