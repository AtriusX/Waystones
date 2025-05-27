package xyz.atrius.waystones.advancement

import org.bukkit.Material
import org.koin.core.annotation.Single
import xyz.atrius.waystones.data.config.Localization
import xyz.atrius.waystones.provider.AdvancementProvider

@Single
class QuantumDomesticationAdvancement(
    localization: Localization,
    secretTunnelAdvancement: SecretTunnelAdvancement,
) : AdvancementProvider(
    title = localization["quantum-domestication"],
    description = localization["quantum-domestication-desc"],
    item = Material.NAME_TAG,
    parent = secretTunnelAdvancement,
)

