# Waystones [![Badge](badge/support.svg)](https://hangar.papermc.io/Atri/Waystones) [![Paper](badge/paper.svg)](https://papermc.io/) [![Kotlin](badge/kotlin.svg)](https://kotlinlang.org)

## Overview
Waystones is a Minecraft plugin designed to integrate teleportation into the survival experience. In many other servers, the use of commands like `/tp` or `/home` can feel overpowered or out of place, stripping away the immersion of utilizing gameplay features to get around.

Waystones addresses this by introducing an item-based teleportation system. By making use of Lodestones and Warp Keys, players can establish a network of fast-travel points across their world. This encourages players to build infrastructure to enable easier fast travel throughout their worlds.

---

## A Guide to Teleportation

### Establishing A Waystone
Teleporting with Waystones requires crafting items and building physical structures in your world.

**1. Obtain a Warp Key**
Depending on how you have set up your server, these can be crafted using the recipe provided below or by crafting a normal compass. If the `enable-key-items` configuration option is enabled, you'll be able to craft it via your servers configured crafting recipe.

The default recipe is as follows:

| | Column 1 | Column 2 | Column 3 | $\rightarrow$ | Result |
| :---: | :---: | :---: | :---: | :---: |:-------------------------------------------------------------------------------------------------------------:|
| **Row 1** | $\text{ }$ | <img src="https://minecraft.wiki/images/Iron_Ingot_JE3_BE2.png" width="48" height="48" alt="Iron Ingot"> | $\text{ }$ | | |
| **Row 2** | <img src="https://minecraft.wiki/images/Iron_Ingot_JE3_BE2.png" width="48" height="48" alt="Iron Ingot"> | <img src="https://minecraft.wiki/images/thumb/Block_of_Redstone_JE2_BE2.png/150px-Block_of_Redstone_JE2_BE2.png" width="48" height="48" alt="Redstone Block"> | <img src="https://minecraft.wiki/images/Iron_Ingot_JE3_BE2.png" width="48" height="48" alt="Iron Ingot"> | $\rightarrow$ | <img width="48" height="48" alt="Key" src="https://github.com/user-attachments/assets/6748ec5e-b867-40b4-9560-58485a61b6bb" /> |
| **Row 3** | $\text{ }$ | <img src="https://minecraft.wiki/images/Iron_Ingot_JE3_BE2.png" width="48" height="48" alt="Iron Ingot"> | $\text{ }$ | | |

**2. Create a Waystone**
Place a Lodestone at a location of your choosing. To link your warp key, right click the Lodestone using the key. Once linked, the item in your hand is tethered to that specific location, until the waystone itself is destroyed.

**3. Warping**
Whenever you wish to return to a fast-travel point, simply right-click the linked Warp Key in your inventory. You will enter a short waiting period (if configured by the server) before being sent back to the location linked to the key.

### Waystone Structures

**Boosting Your Range**
By default, waystones have a limited reach, but you can expand this range by reinforcing the area around your waystone with precious materials. The values in the table below are based on a percentage of your server's configured max-boost property and can be accumulated until you reach the number of blocks equal to your server's configured max-warp-size.

| Material | Boost | Description |
| :--- | :--- | :--- |
| **Netherite** | Maximum | Applies the maximum range, though hard to acquire |
| **Emerald** | 75% Boost | Highly efficient, and easier to get via trading |
| **Diamond** | 50% Boost | Half as effective as netherite, but easier to acquire via mining |
| **Gold** | 33% Boost | Moderate boost, useful for more localized travel |
| **Waxed Copper (Unoxidized)** | 30% Boost | Clean copper gets a slight boost over unwaxed copper of any other variety |
| **Copper** | 25% Boost | Provides a basic but economical boost, good for early game investment |
| **Iron** | 20% Boost | 1/5 as efficient as Netherite, good if you just need a small bump |

**Powering Inter-Dimensional Travel**
Crossing the boundaries between the Overworld, the Nether, and the End requires significant energy. When power requirements are enabled, you must place a **Respawn Anchor** beneath your waystone. Waystones will only function as long as the anchor remains charged. For some addtional convenience, some servers may choose to disable or reconfigure the power cost required to use a waystone.

**The Cost of Convenience: Portal Sickness**
Teleportation can be a taxing process on the body. There is a random chance that a player will arrive at their destination suffering from **Portal Sickness**.

| Effect | Duration | Detail |
| :--- | :--- | :--- |
| **Nausea X** | 30 Seconds | Distorts the player's vision. |
| **Blindness** | 5 Seconds | Severely limits visibility. |
| **Physical Toll** | Variable | May prevent further warping or cause damage if warping while sick. |

**Maintenance**
* **Naming**: Use a Name Tag on your waystone to give it a custom name, making it easier to manage multiple links.
* **Suppression**: If you wish to disable a waystone—perhaps to hide a secret base—place **Obsidian** beneath the lodestone to suppress its functionality.

---

## Admin Tools

### Installation
Be sure to shut down the server before installing the plugin (if it's currently running)

* **Supported Platforms**: Paper-based servers are the only supported installations at this time.
* **Process**: Simply download the plugin JAR and place it into your server's `plugins` directory.

### Commands and Permissions
The following table outlines the commands available to manage the plugin:

| Command | Description | Permission |
| :--- | :--- | :--- |
| `/waystones` | Displays general plugin information | `N/A` |
| `/waystones getkey [count\|player]` | Grants warp keys to specified players | `waystones.getkey.all` / `waystones.getkey.self` |
| `/waystones ratio [set\|remove\|list]` | Manages teleportation distance ratios | `waystones.ratios` |
| `/waystones config [prop] [val]` | Views or updates configuration properties | `waystones.config` |
| `/waystones info` | Displays contributor and plugin details | `N/A` |
| `/waystones reload [type]` | Reloads configuration or advancements | `waystones.reload` |

### Configuration

<details>
<summary>Click to view detailed configuration properties</summary>

| Category | Property | Description |
| :--- | :--- | :--- |
| **Distance & Range** | `limit-distance` | Enable/disable the range limit. |
| | `base-distance` | The default range of a waystone without boosts. |
| | `max-boost` | The maximum additional distance granted per boost block. |
| | `max-warp-size` | Limits how many boost blocks can be utilized. |
| | `world-ratio` | A divisor applied to range when teleporting across dimensions. |
| **Power & Cost** | `require-power` | Determines if anchors are needed (ALL, INTER_DIMENSION, or NONE). |
| | `power-cost` | The amount of charge consumed from an anchor per warp. |
| **Portal Sickness** | `enable-portal-sickness` | Toggles the sickness mechanic. |
| | `portal-sickness-chance` | The probability (0.0 - 1.0) of contracting sickness. |
| | `portal-sickness-warping` | Behavior when sick (PREVENT_TELEPORT, DAMAGE_ON_TELEPORT, ALLOW). |
| | `portal-sickness-damage` | Damage dealt to sick players during a warp. |
| **General Behavior** | `wait-time` | The delay in ticks before teleportation occurs. |
| | `damage-stops-warping` | If true, taking damage cancels the warp sequence. |
| | `single-use` | If true, warp keys are consumed upon use. |
| | `relinkable-keys` | Allows players to link a key to a different waystone. |
| | `enable-key-items` | Toggles the custom item and crafting recipe. |
| | `locale` | Sets the language file for in-game messages. |
</details>

### Built-in Advancements
To enhance the vanilla experience, Waystones offers a collection of achievements that the player can earn:

| Achievement | Requirement |
| :--- | :--- |
| **Waystones** | Link your very first waystone. |
| **Secret Tunnel** | Complete your first successful warp. |
| **1.21 Gigawarps** | Execute a warp covering over 50% of the maximum possible distance. |
| **I Don't Feel So Good** | Suffer the effects of portal sickness. |
| **Heavy Artillery** | Use Netherite to maximize a waystone's power. |
| **Unlimited Power** | Trigger an explosion by overcharging a power source. |
| **Quantum Domestication** | Give a waystone a unique name. |
| **Blocked** | Successfully suppress a waystone using obsidian. |
| **Shoot The Messenger** | Nearly perish to a skeleton while attempting to warp. |
| **Clean Energy** | Utilize a waystone powered by a beacon source. |

---

## Contributing
You can help improve the project by contributing in the following ways:

* **Development**: Submit bug fixes or new feature implementations via Pull Requests on GitHub.
* **Translations**: Provide or refine translations for the localization files to support more languages.
* **Reporting**: Use the GitHub issue tracker to report bugs or suggest improvements.

### Support
If you find Waystones useful and wish to support its continued development, you can:

* **Donate**: You may support the project financially through [GitHub Sponsors](https://github.com/sponsors/AtriusX) or [PayPal](https://paypal.me/Atrius).
* **Recommend**: Share the plugin with other server administrators and players.
