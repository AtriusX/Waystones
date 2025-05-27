package xyz.atrius.waystones.manager

import org.bukkit.entity.Player
import org.koin.core.annotation.Single
import xyz.atrius.waystones.data.advancement.Advancement
import xyz.atrius.waystones.data.config.property.EnableAdvancementsProperty
import xyz.atrius.waystones.internal.KotlinPlugin
import org.bukkit.advancement.Advancement as SpigotAdvancement

@Single
class AdvancementManager(
    private val plugin: KotlinPlugin,
    private val enableAdvancements: EnableAdvancementsProperty,
) {
    private val advancements = mutableSetOf<Advancement>()

    fun register(vararg advancements: Advancement) {
        advancements
            .forEach(this.advancements::add)

        loadAdvancements()
    }

    fun reload() = with(advancements.toTypedArray()) {
        unloadAllAdvancements()
        loadAdvancements()
    }

    fun awardAdvancement(player: Player, adv: Advancement) =
        awardAdvancement(player, plugin.server.getAdvancement(adv.key()))

    fun awardAdvancement(player: Player, adv: SpigotAdvancement?) {
        if (adv == null || !enableAdvancements.value) {
            return
        }

        val criteria = player
            .getAdvancementProgress(adv)

        criteria.remainingCriteria
            .forEach(criteria::awardCriteria)
    }

    @Suppress("DEPRECATION")
    private fun unloadAllAdvancements() = advancements.forEach {
        plugin.server.unsafe.removeAdvancement(it.key())
    }

    @Suppress("DEPRECATION")
    private fun loadAdvancements() = advancements.forEach {
        val (key, adv) = it.paired()
        val server = plugin.server
        val spigotAdv = server.getAdvancement(key)

        if (spigotAdv == null) {
            server.unsafe.loadAdvancement(key, adv.toJson())
        }
    }
}