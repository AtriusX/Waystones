package xyz.atrius.waystones.advancement

import org.bukkit.Material
import xyz.atrius.waystones.manager.LocalizationManager
import xyz.atrius.waystones.provider.AdvancementProvider

// @Single Disabled until the advancement is useful
class HowLowCanYouGoAdvancement(
    localization: LocalizationManager,
    secretTunnelAdvancement: SecretTunnelAdvancement,
) : AdvancementProvider(
    title = localization["how-low-can-you-go"],
    description = localization["how-low-can-you-go-desc"],
    item = Material.BLUE_CONCRETE_POWDER,
    parent = secretTunnelAdvancement,
)
