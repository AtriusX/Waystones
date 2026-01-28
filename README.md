
# Waystones [![Badge](badge/support.svg)](https://www.spigotmc.org/resources/waystones.93917/) [![Paper](badge/paper.svg)](https://papermc.io/) [![Kotlin](badge/kotlin.svg)](https://kotlinlang.org)

Waystones is a minecraft server plugin that brings teleportation to your worlds without needing to rely on commands. This plugin works great in survival worlds where command teleporting is often out of place or just overpowered in general.  
  
## How to use it:  
  
Players can create and warp to a waystone by creating a lodestone and linking it with a compass or warp key (more on this later). When the player right-clicks the warp key, they will initiate a teleport to the linked waystone.  

#### Other Features:

- When single use warp keys is enabled, warp keys will be destroyed upon warping.
- When distance limiting is enabled, users can place blocks around the warp stone to increase its max range!
	- Supported blocks include **Netherite (max boost)**, **Emerald (75% boost)**, **Diamond (50% boost)**, **Gold (33% boost)**, **Waxed Copper Block (30% Boost)**, **Other Copper Blocks (25% Boost)** or **Iron (20% boost)** blocks.
	- Obsidian can be placed under a lodestone to prevent it from being used as a waystone!
	- For admins, a **command block** can be placed under a lodestone to remove the range limit and power requirements!
- When power requirements are enabled, users can place a respawn anchor under the waystone to allow for teleportation!
	- This can be set for all warps or just ones between dimensions!
	- The waystone will work as long as the respawn anchor remains charged.
	- Respawn anchors can be auto-recharged upon warping!
	- A ratio can be applied to the warp range to divide the max range of interdimensional warps.
- Waystones can be named using a name tag!
- When portal sickness is enabled, players have a random chance of being hit with this effect upon teleporting!
	- Grants **Nausea X** for 30 seconds and **Blindness** for 5 seconds.
	- If sick warping is set, then players can be blocked from warping until they recover, or take damage from subsequent warps if they are sick!
- A crafting recipe for warp keys can be enabled.
	- This allows compasses to function as normal, and creates a custom item to use as the warp key!

## Crafting a Warp Key:

The default crafting recipe for the warp key is as follows:

<img alt="Warp Key Recipe" src="https://i.imgur.com/N2t9pn1.png" width="500"/>

Please note that this recipe is only used if **enable-key-items** is set to `true` in the configuration file. In future updates this recipe may also potentially be customizable via the config.

### Configuration Options:  
  
- **locale *[default: en]***: The localization file to be used (named lang-locale.yml). This will also affect how numbers or date are formatted if the locale matches any [BCP 47](https://www.iana.org/assignments/language-subtag-registry/language-subtag-registry) language tag, or the language/country neutral format will be used.
- **wait-time *[default: 60]***: The amount of time (in ticks) to wait before teleporting to the waystone. Moving will cancel the teleport.
- **damage-stops-warping *[default: true]***: Whether taking damage will prevent the player from teleporting.
- **limit-distance *[default: true]***: Limits the range of a waystone to the base distance plus the total boost value.
- **base-distance *[default: 100]***: The minimum amount of blocks in range a waystone will have.
- **max-boost *[default: 150]***: The max boost amount permitted per block of a waystone. 
- **max-warp-size *[default: 50]***: The max number of boost blocks a warp is able to use.
- **jump-worlds *[default: true]***: Whether teleporting across dimensions is allowed.
- **world-ratio *[default: 8]***: The amount of blocks to divide the max warp range by during inter-dimensional teleports. 
- **warp-animations *[default: true]***: Whether to play a particle animation while the player waits to teleport.
- **single-use *[default: false]***: Whether to dispose of the warp key once it is successfully used.
- **require-power *[default: INTER_DIMENSION]***: Whether power is required for warping. Can be set to INTER_DIMENSION for only inter-dimensional travel, ALL for any warps, or NONE to disable.
- **power-cost *[default: 1]***: How much power a warp will require if `require-power` is set to INTER_DIMENSION or ALL.
- **enable-portal-sickness *[default: true]***: Whether players can be hit with a sickness effect if they teleport.
- **portal-sickness-chance *[default: 0.05]***: The chance at which portal sickness can be applied to the player.
- **portal-sickness-warping *[default: DAMAGE_ON_TELEPORT]***: Whether portal sickness has an adverse effect on warping. Can be set to PREVENT_TELEPORT to stop players from warping while sick, DAMAGE_ON_TELEPORT to allow players to warp but take damage from it while sick, or ALLOW to disable any adverse effects.
- **portal-sickness-damage *[default: 5.0]***: The amount of damage (in heart pieces) taken by the user if warping while sick and DAMAGE_ON_TELEPORT is set.
- **relinkable-keys *[default: true]***: Whether warp keys can be relinked after they are linked to a waystone.
- **enable-key-items *[default: true]***: Whether to use a custom item for warp keys. This enables a custom crafting recipe for the item, and prevents normal compasses from being used as warp keys. If this setting is enabled later, compass warp keys *should* hopefully still work as warp keys.
- **key-recipe *[default: shown above]***: The crafting recipe used for custom key items if custom keys are enabled.
- **enable-advancements *[default: true]***: Whether advancements are enabled.

### Commands

- **/waystones**: Provides info about the plugin
- **/waystones getkey [ count | player [count] ]**: Gives warp keys to players
- **/waystones ratio [ set <value> [name] [-environment] | remove [name] [-environment] | list ]**: Manages the plugin's ratios
- **/waystones config [property] [new value]**: Allows users to view and update the plugin configuration
- **/waystones info**: Displays information about the plugin and its contributors
- **/waystones reload [config | advancements]**: Reloads the plugin's config or advancements.

### Permissions

- **waystones.link**: Allows users to link waystones to a warp key
- **waystones.getkey.self**: Allows using the `getkey` command to give yourself a warp key
- **waystones.getkey.all**: Allows using the `getkey` command to give any player a warp key
- **waystones.config**: Allows viewing and modifying the plugin configuration
- **waystones.reload**: Allows reloading of the plugin configuration
- **waystones.ratios**: Allows use of the `ratio` command to manage teleport ratios

### Advancements

As of version 1.1.0 we now include support for advancements! Below is a list of the advancements currently provided by Waystones. 

- **Waystones**: Link a waystone to a warp key.
- **Secret Tunnel**: User a waystone for the first time.
- **1.21 Gigawarps**: Travel over 50% of the max warp distance using a waystone.
- **I Don't Feel So Good**: Get hit with portal sickness from using a waystone.
- **Heavy Artillery**: Power up a waystone with a netherite block.
- **Unlimited Power**: Overcharge a waystone's power source causing it to explode.
- **Quantum Domestication**: Give a waystone a name.
- **Blocked**: Suppress a waystone with obsidian to prevent people from using it.
- **Shoot The Messenger**: Nearly die from a skeleton while attempting to warp away.
- **Clean Energy**: Use a waystone that's powered off the same energy source as a beacon.