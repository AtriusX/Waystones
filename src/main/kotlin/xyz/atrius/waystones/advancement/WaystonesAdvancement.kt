package xyz.atrius.waystones.advancement

import org.bukkit.Material
import org.koin.core.annotation.Single
import xyz.atrius.waystones.manager.LocalizationManager
import xyz.atrius.waystones.provider.AdvancementProvider

@Single
class WaystonesAdvancement(
    localization: LocalizationManager,
) : AdvancementProvider(
    title = localization["waystones"],
    description = localization["waystones-desc"],
    item = Material.LODESTONE,
)
