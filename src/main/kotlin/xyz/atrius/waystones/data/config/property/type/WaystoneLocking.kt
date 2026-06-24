package xyz.atrius.waystones.data.config.property.type

/**
 * Determines how waystone ownership and locking behaves on the server.
 *
 * This enum controls whether waystones can be owned by players and how
 * that ownership affects key linking permissions.
 */
enum class WaystoneLocking {
    /**
     * All waystones are automatically locked to the player who first links them.
     * Only the owner can link keys to the waystone. This provides maximum
     * protection but removes the ability for players to share waystones.
     */
    ALL,

    /**
     * Waystone owners can individually toggle lock status per waystone.
     * When a waystone is locked, only the owner can link keys to it.
     * When unlocked, anyone can link keys. This provides flexible
     * ownership while allowing sharing when desired.
     */
    PER_WAYSTONE,

    /**
     * No ownership or locking is enforced. Any player can link keys
     * to any waystone regardless of who placed it. This is the default
     * behavior and maintains backward compatibility with versions
     * before ownership was introduced.
     */
    NONE
}
