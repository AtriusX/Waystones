package xyz.atrius.waystones.data.config.property

import org.koin.core.annotation.Single
import xyz.atrius.waystones.SicknessOption
import xyz.atrius.waystones.command.resolver.EnumArgumentType
import xyz.atrius.waystones.data.config.ConfigProperty

@Single
class PortalSicknessWarpingProperty : ConfigProperty<SicknessOption>(
    property = "portal-sickness-warping",
    default = SicknessOption.DAMAGE_ON_TELEPORT,
    parser = EnumArgumentType(SicknessOption::class),
    propertyType = SicknessOption::class,
)