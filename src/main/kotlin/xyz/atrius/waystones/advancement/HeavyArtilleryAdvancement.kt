package xyz.atrius.waystones.advancement

import org.bukkit.Material
import org.koin.core.annotation.Single
import xyz.atrius.waystones.data.json.advancement.AdvancementType
import xyz.atrius.waystones.manager.LocalizationManager
import xyz.atrius.waystones.provider.AdvancementProvider

@Single
class HeavyArtilleryAdvancement(
    localization: LocalizationManager,
    secretTunnelAdvancement: SecretTunnelAdvancement,
) : AdvancementProvider(
    title = localization["heavy-artillery"],
    description = localization["heavy-artillery-desc"],
    item = Material.NETHERITE_BLOCK,
    parent = secretTunnelAdvancement,
    type = AdvancementType.GOAL,
)
