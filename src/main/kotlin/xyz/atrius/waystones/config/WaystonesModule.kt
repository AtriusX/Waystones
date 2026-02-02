package xyz.atrius.waystones.config

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.bstats.bukkit.Metrics
import org.bukkit.Material
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import xyz.atrius.waystones.data.json.serializer.MaterialTypeAdapter
import xyz.atrius.waystones.internal.KotlinPlugin
import java.util.Locale

const val BSTATS_PLUGIN_ID: Int = 29245

@Module
@ComponentScan("xyz.atrius.waystones")
object WaystonesModule {

    @Single
    fun gson(): Gson = GsonBuilder()
        .setPrettyPrinting()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .registerTypeAdapter(Material::class.java, MaterialTypeAdapter)
        .create()

    @Single
    @Named("supportedLocales")
    fun supportedLocales(): Set<Locale> = setOf(
        Locale.ENGLISH,
        Locale.CHINESE,
    )

    @Single
    @Named("defaultPluginLocale")
    fun defaultPluginLocale(): Locale = Locale.ENGLISH

    @Single(createdAtStart = true)
    fun metrics(plugin: KotlinPlugin): Metrics =
        Metrics(plugin, BSTATS_PLUGIN_ID)
}
