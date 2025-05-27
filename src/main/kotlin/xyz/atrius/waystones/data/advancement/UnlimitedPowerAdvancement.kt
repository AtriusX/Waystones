package xyz.atrius.waystones.data.advancement

import org.bukkit.Material
import org.koin.core.annotation.Single
import xyz.atrius.waystones.data.config.Localization
import xyz.atrius.waystones.data.json.advancement.AdvancementType

@Single
class UnlimitedPowerAdvancement(
    localization: Localization,
    heavyArtilleryAdvancement: HeavyArtilleryAdvancement,
) : AdvancementProvider(
    title = localization["unlimited-power"],
    description = localization["unlimited-power-desc"],
    item = Material.GLOWSTONE,
    parent = heavyArtilleryAdvancement,
    type = AdvancementType.GOAL,
)