package xyz.atrius.waystones.data.advancement

import org.bukkit.Material
import org.koin.core.annotation.Single
import xyz.atrius.waystones.data.config.Localization

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

