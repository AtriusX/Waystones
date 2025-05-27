package xyz.atrius.waystones.data.advancement

import org.bukkit.Material
import xyz.atrius.waystones.data.config.Localization

//@Single Disabled until the advancement is useful
class HowLowCanYouGoAdvancement(
    localization: Localization,
    secretTunnelAdvancement: SecretTunnelAdvancement,
) : AdvancementProvider(
    title = localization["how-low-can-you-go"],
    description = localization["how-low-can-you-go-desc"],
    item = Material.BLUE_CONCRETE_POWDER,
    parent = secretTunnelAdvancement,
)