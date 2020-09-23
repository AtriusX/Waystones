
# Warpstones  
Warpstones is a minecraft server plugin that brings teleportation to your worlds without needing to rely on commands. This plugin works great in survival worlds where command teleporting is often out of place or just overpowered in general.  
  
## How to use it:  
  
Players can create and warp to a warpstone by creating a lodestone and linking it with a compass or warp key (more on this later). When the player right-clicks the warp key, they will initiate a teleport to the linked warpstone.  
#### Other Features:
- When single use warp keys is enabled, warp keys will be destroyed upon warping.
- When distance limiting is enabled, users can place blocks around the warp stone to increase it's max range!
	- Supported blocks include **Netherite (max boost)**, **Emerald (75% boost)**, **Diamond (50% boost)**, **Gold (33% boost)** or **Iron (20% boost)** blocks.
	- Obsidian can be placed under a lodestone to prevent it from being used as a warpstone!
	- For admins, a **command block** can be placed under a lodestone to remove the range limit and power requirements!
- When power requirements are enabled, users can place a respawn anchor under the warpstone to allow for teleportation!
	- This can be set for all warps or just ones between dimensions!
	- The warpstone will work as long as the respawn anchor remains charged.
	- Respawn anchors can be auto-recharged upon warping!
	- A ratio can be applied to the warp range to divide the max range of interdimensional warps.
- Warpstones can be named using a nametag!
- When portal sickness is enabled, players have a random chance of being hit with this effect upon teleporting!
	- Grants **Nausea X** for 30 seconds and **Blindness** for 5 seconds.
	- If sick warping is set, then players can be blocked from warping until they recover, or take damage from subsequent warps if they are sick!
- A crafting recipe for warp keys can be enabled.
	- This allows compasses to function as normal, and creates a custom item to use as the warp key!

### Configuration Options:  
  
- **wait-time *[default: 60]***: The amount of time (in ticks) to wait before teleporting to the warpstone. Moving will cancel the teleport.
- **damage-stops-warping *[default: true]***: Whether or not taking damage will prevent the player from teleporting.
- **limit-distance *[default: true]***: Limits the range of a warpstone to the base distance plus the total boost value.
- **base-distance *[default: 100]***: The minimum amount of blocks in range a warpstone will have.
- **max-boost: *[default: 150]***: The max boost amount permitted per block of a warpstone. 
- **max-warp-size *[default: 50]***: The max number of boost blocks a warp is able to use.
- **jump-worlds *[default: true]***: Whether or not teleporting across dimensions is allowed.
- **world-ratio *[default: 8]***: The amount of blocks to divide the max warp range by during inter-dimensional teleports. 
- **warp-animations *[default: true]***: Whether or not to play a particle animation while the player waits to teleport.
- **single-use *[default: false]***: Whether or not to dispose of the warp key once it is successfully used.
- **require-power *[default: INTER_DIMENSION]***: Whether power is required for warping. Can be set to INTER_DIMENSION for only inter-dimensional travel, ALL for any warps, or NONE to disable.
- **enable-portal-sickness *[default: true]***: Whether or not players can be hit with a sickness effect if they teleport.
- **portal-sickness-chance *[default: 0.05]***: The chance at which portal sickness can be applied to the player.
- **portal-sickness-warping *[default: DAMAGE_ON_TELEPORT]***: Whether portal sickness has an adverse effect on warping. Can be set to PREVENT_TELEPORT to stop players from warping while sick, DAMAGE_ON_TELEPORT to allow players to warp but take damage from it while sick, or ALLOW to disable any adverse effects.
- **portal-sickness-damage *[default: 5.0]***: The amount of damage (in heart pieces) taken by the user if warping while sick and DAMAGE_ON_TELEPORT is set.
- **relinkable-keys *[default: true]***: Whether or not warp keys can be relinked after they are linked to a warpstone.
- **enable-key-items *[default: true]***: Whether or not to use a custom item for warp keys. This enables a custom crafting recipe for the item, and prevents normal compasses from being used as warp keys. If this setting is enabled later, compass warp keys *should* hopefully still work as warp keys.
