package xyz.atrius.waystones.dao

import java.util.UUID

/**
 * Represents an authorized user entry for a waystone.
 *
 * @property waystoneUuid The UUID of the waystone this authorization belongs to
 * @property ownerUuid The UUID of the authorized player
 */
data class WaystoneAuthorizedUser(
    val waystoneUuid: UUID,
    val ownerUuid: UUID,
)
