package xyz.atrius.waystones.advancement

import org.bukkit.Material
import org.koin.core.annotation.Single
import xyz.atrius.waystones.data.config.Localization
import xyz.atrius.waystones.provider.AdvancementProvider

@Single
class WaystonesAdvancement(
    localization: Localization,
) : AdvancementProvider(
    title = localization["waystones"],
    description = localization["waystones-desc"],
    item = Material.LODESTONE,
)