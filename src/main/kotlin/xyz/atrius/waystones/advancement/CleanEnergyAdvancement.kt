package xyz.atrius.waystones.advancement

import org.bukkit.Material
import org.koin.core.annotation.Single
import xyz.atrius.waystones.data.config.Localization
import xyz.atrius.waystones.data.json.advancement.AdvancementType
import xyz.atrius.waystones.provider.AdvancementProvider

@Single
class CleanEnergyAdvancement(
    localization: Localization,
    heavyArtilleryAdvancement: HeavyArtilleryAdvancement,
) : AdvancementProvider(
    title = localization["clean-energy"],
    description = localization["clean-energy-desc"],
    item = Material.BEACON,
    parent = heavyArtilleryAdvancement,
    type = AdvancementType.GOAL,
)
