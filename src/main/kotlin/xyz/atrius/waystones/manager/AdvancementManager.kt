package xyz.atrius.waystones.manager

import org.koin.core.annotation.Single
import xyz.atrius.waystones.data.advancement.Advancement
import xyz.atrius.waystones.internal.KotlinPlugin
import xyz.atrius.waystones.utility.loadAdvancements
import xyz.atrius.waystones.utility.unloadAdvancements

@Single
class AdvancementManager(
    private val plugin: KotlinPlugin,
) {
    private val advancements = setOf<Advancement>()

    fun register(vararg advancements: Advancement) {
        this.advancements + advancements
        plugin.loadAdvancements(*advancements)
    }

    fun reload() = with(advancements.toTypedArray()) {
        plugin.unloadAdvancements(*this)
        plugin.loadAdvancements(*this)
    }
}