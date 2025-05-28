package xyz.atrius.waystones

import org.koin.core.Koin
import org.koin.ksp.generated.module
import xyz.atrius.waystones.config.PluginModule
import xyz.atrius.waystones.internal.KotlinPlugin
import xyz.atrius.waystones.internal.PluginEntrypoint

@PluginEntrypoint
class Waystones : KotlinPlugin(PluginModule.module) {

    override fun enable(koin: Koin) =
        enablePlugin<WaystonesInitializer>(koin)

    override fun disable(koin: Koin) =
        disablePlugin<WaystonesInitializer>(koin)
}

