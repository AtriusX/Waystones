package xyz.atrius.waystones.internal

import org.bukkit.plugin.java.JavaPlugin
import org.koin.core.Koin
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

open class KotlinPlugin(private vararg val modules: Module) : JavaPlugin() {
    private val koin: Koin = startKoin {
        // Load modules into scope
        modules(defaultModule(), *modules)
    }.koin

    open fun enable(koin: Koin) {}

    open fun disable(koin: Koin) {}

    open fun defaultModule(): Module = module {
        single { this@KotlinPlugin }
    }

    final override fun onEnable() {
        config.options().copyDefaults(true)
        saveConfig()
        enable(koin)
    }

    final override fun onDisable() = disable(koin)

    inline fun <reified T : PluginInitializer> enablePlugin(koin: Koin) {
        koin.get<T>().enable(this)
    }

    inline fun <reified T : PluginInitializer> disablePlugin(koin: Koin) {
        koin.get<T>().disable(this)
    }
}
