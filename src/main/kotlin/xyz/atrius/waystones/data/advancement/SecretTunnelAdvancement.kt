package xyz.atrius.waystones.data.advancement

import org.bukkit.Material
import org.koin.core.annotation.Single
import xyz.atrius.waystones.data.config.Localization

@Single
class SecretTunnelAdvancement(
    localization: Localization,
    waystonesAdvancement: WaystonesAdvancement,
) : AdvancementProvider(
    title = localization["secret-tunnel"],
    description = localization["secret-tunnel-desc"],
    item = Material.COMPASS,
    parent = waystonesAdvancement
)