package xyz.atrius.waystones.advancement

import org.bukkit.Material
import org.koin.core.annotation.Single
import xyz.atrius.waystones.data.json.advancement.AdvancementType
import xyz.atrius.waystones.manager.LocalizationManager
import xyz.atrius.waystones.provider.AdvancementProvider

@Single
class UnlimitedPowerAdvancement(
    localization: LocalizationManager,
    heavyArtilleryAdvancement: HeavyArtilleryAdvancement,
) : AdvancementProvider(
    title = localization["unlimited-power"],
    description = localization["unlimited-power-desc"],
    item = Material.GLOWSTONE,
    parent = heavyArtilleryAdvancement,
    type = AdvancementType.GOAL,
)
