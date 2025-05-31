package xyz.atrius.waystones.data.json

import java.util.UUID

data class RatioConfig(
    var defaultWorld: UUID?,
    var ratios: HashMap<UUID, Double>?,
)
