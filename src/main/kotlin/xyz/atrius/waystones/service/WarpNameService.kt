package xyz.atrius.waystones.service

import com.google.gson.Gson
import org.koin.core.annotation.Single
import xyz.atrius.waystones.data.JsonFile
import xyz.atrius.waystones.internal.KotlinPlugin

@Single
@Deprecated("Database support is included now, so this will be phased out in the future")
class WarpNameService(
    gson: Gson,
    plugin: KotlinPlugin,
) : JsonFile<HashMap<String, String>>("warpnames", gson, plugin, HashMap<String, String>()::class)
