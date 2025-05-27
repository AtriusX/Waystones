package xyz.atrius.waystones.advancement

import org.bukkit.Material
import org.koin.core.annotation.Single
import xyz.atrius.waystones.data.config.Localization
import xyz.atrius.waystones.provider.AdvancementProvider

@Single
class IDontFeelSoGoodAdvancement(
    localization: Localization,
    secretTunnelAdvancement: SecretTunnelAdvancement,
) : AdvancementProvider(
    title = localization["i-dont-feel-so-good"],
    description = localization["i-dont-feel-so-good-desc"],
    item = Material.WITHER_ROSE,
    parent = secretTunnelAdvancement,
)