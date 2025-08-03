package xyz.atrius.waystones.dao

import java.util.UUID

data class WaystoneInfo(
    val worldUid: UUID,
    val x: Int,
    val y: Int,
    val z: Int,
    val name: String? = null,
)
