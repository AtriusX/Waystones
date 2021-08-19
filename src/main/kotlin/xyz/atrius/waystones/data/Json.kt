package xyz.atrius.waystones.data

import com.google.gson.Gson

interface Json<T> {

    fun toJsonString(): String =
        Gson().toJson(this)

    companion object {
        inline fun <reified  T : Json<T>> parse(input: String): T =
            Gson().fromJson(input, T::class.java)
    }
}