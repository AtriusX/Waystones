package xyz.atrius.waystones.advancement

import org.bukkit.Material
import org.koin.core.annotation.Single
import xyz.atrius.waystones.manager.LocalizationManager
import xyz.atrius.waystones.provider.AdvancementProvider

@Single
class IDontFeelSoGoodAdvancement(
    localization: LocalizationManager,
    secretTunnelAdvancement: SecretTunnelAdvancement,
) : AdvancementProvider(
    title = localization["i-dont-feel-so-good"],
    description = localization["i-dont-feel-so-good-desc"],
    item = Material.WITHER_ROSE,
    parent = secretTunnelAdvancement,
)
