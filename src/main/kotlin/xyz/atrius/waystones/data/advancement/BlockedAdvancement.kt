package xyz.atrius.waystones.data.advancement

import org.bukkit.Material
import org.koin.core.annotation.Single
import xyz.atrius.waystones.data.config.Localization

@Single
class BlockedAdvancement(
    localization: Localization,
    secretTunnelAdvancement: SecretTunnelAdvancement,
) : AdvancementProvider(
    title = localization["blocked"],
    description = localization["blocked"],
    item = Material.OBSIDIAN,
    parent = secretTunnelAdvancement,
)