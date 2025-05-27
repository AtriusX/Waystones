package xyz.atrius.waystones.manager

import com.google.gson.Gson
import org.bukkit.entity.Player
import org.koin.core.annotation.Single
import org.slf4j.LoggerFactory
import xyz.atrius.waystones.provider.AdvancementProvider
import xyz.atrius.waystones.data.config.property.EnableAdvancementsProperty
import xyz.atrius.waystones.internal.KotlinPlugin
import org.bukkit.advancement.Advancement as SpigotAdvancement

@Single
class AdvancementManager(
    private val plugin: KotlinPlugin,
    private val enableAdvancements: EnableAdvancementsProperty,
    private val gson: Gson,
    private val advancementContainers: List<AdvancementProvider>,
) {
    private val server = plugin.server

    fun loadAdvancements() {
        logger.info("Loading ${advancementContainers.size} advancements...")

        val groups = advancementContainers
            .groupBy { it.asAdvancement.parent }

        load(groups)
        logger.info("Loading complete!")
    }

    @Suppress("DEPRECATION")
    private fun load(groups: Map<String?, List<AdvancementProvider>>, current: String? = null) {
        val current = groups[current]

        if (current == null) {
            return
        }

        for (item in current) {
            logger.info("Loading advancement '${item.namespacedKey().asString()}'")
            server.unsafe.loadAdvancement(item.namespacedKey(), gson.toJson(item.asAdvancement))
            load(groups, item.namespacedKey().asString())
        }
    }


    @Suppress("DEPRECATION")
    private fun unloadAllAdvancements() {
        logger.info("Unloading advancements!")

        advancementContainers.forEach {
            server.unsafe.removeAdvancement(it.namespacedKey())
        }
    }

    fun reload() {
        unloadAllAdvancements()
        loadAdvancements()
    }

    fun awardAdvancement(player: Player, adv: AdvancementProvider) =
        awardAdvancement(player, plugin.server.getAdvancement(adv.namespacedKey()))

    fun awardAdvancement(player: Player, adv: SpigotAdvancement?) {
        if (adv == null || !enableAdvancements.value) {
            return
        }

        val criteria = player
            .getAdvancementProgress(adv)

        criteria.remainingCriteria
            .forEach(criteria::awardCriteria)
    }

    companion object {

        private val logger = LoggerFactory
            .getLogger(AdvancementManager::class.java)
    }
}