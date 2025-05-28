package xyz.atrius.waystones.advancement

import org.bukkit.Material
import org.koin.core.annotation.Single
import xyz.atrius.waystones.data.config.Localization
import xyz.atrius.waystones.data.json.advancement.AdvancementType
import xyz.atrius.waystones.provider.AdvancementProvider

@Single
class GigawarpsAdvancement(
    localization: Localization,
    cleanEnergyAdvancement: CleanEnergyAdvancement,
) : AdvancementProvider(
    title = localization["gigawarps"],
    description = localization["gigawarps-desc"],
    item = Material.REDSTONE_BLOCK,
    parent = cleanEnergyAdvancement,
    type = AdvancementType.CHALLENGE,
)
