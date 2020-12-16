package xyz.atrius.waystones.data.config

import org.bukkit.configuration.file.FileConfiguration
import xyz.atrius.waystones.Power
import xyz.atrius.waystones.SicknessOption
import xyz.atrius.waystones.utility.KotlinPlugin
import xyz.atrius.waystones.utility.valueOfOrDefault
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

class Config(
    private val plugin: KotlinPlugin
) {
    private val config: FileConfiguration
        get() = plugin.config

    // The amount of ticks to wait before teleporting
    var waitTime: Int by property("wait-time", 60)

    // Whether or not the player receiving damage will force cancel the warp
    var damageStopsWarping: Boolean by property("damage-stops-warping", true)

    // Determines if teleporters limit their max distance or not, will check if user is in range before teleporting if true
    var limitDistance: Boolean by property("limit-distance", true)

    // The minimum base range a waystone can have
    var baseDistance: Int by property("base-distance", 100)

    // The maximum amount of blocks a portal block can boost the range of a portal (this is for netherite)
    var maxBoost: Int by property("max-boost", 50)

    // The max number of blocks allowed in warp structures
    var maxWarpSize: Int by property("max-warp-size", 25)

    // Allow users to jump between dimensions when warping
    var jumpWorlds: Boolean by property("jump-worlds", true)

    // How many blocks it takes to travel one block in the destination world
    var worldRatio: Int by property("world-ratio", 8)

    // Play animations during teleport sequence
    var warpAnimations: Boolean by property("warp-animations", true)

    // Determines whether or not warping with a compass deletes the item after use
    var singleUse: Boolean by property("single-use", false)

    // Determines if/how power is required from a respawn anchor in order to use the warp
    var requirePower: Power by enumProp("require-power", Power.INTER_DIMENSION)

    // Whether or not debuff effects are enabled
    var portalSickness: Boolean by property("enable-portal-sickness", true)

    // The chance at which debuff effects can occur (default is 5%)
    var portalSicknessChance: Double by property("portal-sickness-chance", 0.05)

    var portalSickWarping: SicknessOption by enumProp("portal-sickness-warping", SicknessOption.DAMAGE_ON_TELEPORT)

    var portalSicknessDamage: Double by property("portal-sickness-damage", 5.0)

    // Whether or not keys can be relinked to another warpstone after being linked once
    var relinkableKeys: Boolean by property("relinkable-keys", true)

    var keyItems: Boolean by property("enable-key-items", true)

    // Netherite grants the max amount of boost per block
    val netheriteBoost: Int
        get() = maxBoost
    // Emerald grants 75% of the max boost per block
    val emeraldBoost: Int
        get() = (maxBoost * 0.75).toInt()
    // Diamond grants 50% of the max boost per block
    val diamondBoost: Int
        get() = maxBoost / 2
    // Gold grants 33% of the max boost per block
    val goldBoost: Int
        get() = maxBoost / 3
    // Iron grants 20% of the max boost per block
    val ironBoost: Int
        get() = maxBoost / 5

    init {
        plugin.config.options().copyDefaults(true)
        plugin.saveDefaultConfig()
    }

    fun <T> update(property: String, new: T) {
        config.set(property, new)
        plugin.saveConfig()
    }

    private inline fun <reified T> property(
            property: String, default: T
    ) = Delegates.observable(
        config.get(property) as? T? ?: default, observe(property)
    )

    private inline fun <reified T : Enum<T>> enumProp(
            property: String, default: T
    ) = Delegates.observable(
        valueOfOrDefault(config.getString(property), default), observe(property)
    )

    private fun <T> observe(property: String) = { _: KProperty<*>, _: T, new: T ->
        update(property, new)
    }
}