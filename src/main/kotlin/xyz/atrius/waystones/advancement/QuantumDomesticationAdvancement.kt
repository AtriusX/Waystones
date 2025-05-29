package xyz.atrius.waystones.advancement

import org.bukkit.Material
import org.koin.core.annotation.Single
import xyz.atrius.waystones.manager.LocalizationManager
import xyz.atrius.waystones.provider.AdvancementProvider

@Single
class QuantumDomesticationAdvancement(
    localization: LocalizationManager,
    secretTunnelAdvancement: SecretTunnelAdvancement,
) : AdvancementProvider(
    title = localization["quantum-domestication"],
    description = localization["quantum-domestication-desc"],
    item = Material.NAME_TAG,
    parent = secretTunnelAdvancement,
)
