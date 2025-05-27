package xyz.atrius.waystones.data.advancement

import org.bukkit.Material
import org.koin.core.annotation.Single
import xyz.atrius.waystones.data.config.Localization

@Single
class WaystonesAdvancement(
    localization: Localization,
) : AdvancementProvider(
    title = localization["waystones"],
    description = localization["waystones-desc"],
    item = Material.LODESTONE,
)