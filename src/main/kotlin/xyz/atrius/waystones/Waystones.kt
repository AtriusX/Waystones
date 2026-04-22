package xyz.atrius.waystones

import org.koin.core.annotation.KoinApplication
import org.koin.dsl.module
import org.koin.plugin.module.dsl.startKoin
import xyz.atrius.waystones.config.DatabaseModule
import xyz.atrius.waystones.config.WaystonesModule
import xyz.atrius.waystones.internal.KotlinPlugin
import xyz.atrius.waystones.internal.PluginEntrypoint

@KoinApplication(modules = [WaystonesModule::class, DatabaseModule::class])
class WaystonesKoinApp

typealias KoinApp =
    org.koin.core.KoinApplication

@PluginEntrypoint
class Waystones : KotlinPlugin() {

    private lateinit var koin: KoinApp

    private fun defaultModule() = module {
        single<KotlinPlugin> { this@Waystones }
    }

    override fun onEnable() {
        config.options().copyDefaults(true)
        saveConfig()

        koin = startKoin<WaystonesKoinApp> {
            modules(defaultModule())
        }
        koin.koin.get<WaystonesInitializer>().enable(this)
    }

    override fun onDisable() {
        koin.koin.get<WaystonesInitializer>().disable(this)
    }
}
