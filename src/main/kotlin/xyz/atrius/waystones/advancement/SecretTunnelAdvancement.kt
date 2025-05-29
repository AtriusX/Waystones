package xyz.atrius.waystones.advancement

import org.bukkit.Material
import org.koin.core.annotation.Single
import xyz.atrius.waystones.manager.LocalizationManager
import xyz.atrius.waystones.provider.AdvancementProvider

@Single
class SecretTunnelAdvancement(
    localization: LocalizationManager,
    waystonesAdvancement: WaystonesAdvancement,
) : AdvancementProvider(
    title = localization["secret-tunnel"],
    description = localization["secret-tunnel-desc"],
    item = Material.COMPASS,
    parent = waystonesAdvancement
)
