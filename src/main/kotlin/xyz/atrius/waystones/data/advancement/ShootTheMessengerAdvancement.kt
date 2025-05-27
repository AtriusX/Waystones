package xyz.atrius.waystones.data.advancement

import org.bukkit.Material
import org.koin.core.annotation.Single
import xyz.atrius.waystones.data.config.Localization
import xyz.atrius.waystones.data.json.advancement.AdvancementType

@Single
class ShootTheMessengerAdvancement(
    localization: Localization,
    unlimitedPowerAdvancement: UnlimitedPowerAdvancement,
) : AdvancementProvider(
    title = localization["shoot-the-messenger"],
    description = localization["shoot-the-messenger-desc"],
    item = Material.SKELETON_SKULL,
    parent = unlimitedPowerAdvancement,
    type = AdvancementType.CHALLENGE,
)