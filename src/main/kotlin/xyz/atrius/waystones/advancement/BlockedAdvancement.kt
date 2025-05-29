package xyz.atrius.waystones.advancement

import org.bukkit.Material
import org.koin.core.annotation.Single
import xyz.atrius.waystones.manager.LocalizationManager
import xyz.atrius.waystones.provider.AdvancementProvider

@Single
class BlockedAdvancement(
    localization: LocalizationManager,
    secretTunnelAdvancement: SecretTunnelAdvancement,
) : AdvancementProvider(
    title = localization["blocked"],
    description = localization["blocked"],
    item = Material.OBSIDIAN,
    parent = secretTunnelAdvancement,
)
