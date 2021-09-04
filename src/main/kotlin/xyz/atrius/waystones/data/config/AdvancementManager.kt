package xyz.atrius.waystones.data.config

import xyz.atrius.waystones.data.advancement.Advancement
import xyz.atrius.waystones.plugin
import xyz.atrius.waystones.utility.loadAdvancements
import xyz.atrius.waystones.utility.unloadAdvancements


object AdvancementManager {

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