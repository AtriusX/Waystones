package xyz.atrius.waystones.data.json.advancement

data class Display(
    val title: Text,
    val description: Text,
    val icon: Icon,
    val showToast: Boolean = true,
    val announceToChat: Boolean = true,
    val hidden: Boolean =  false,
    val frame: AdvancementType? = null,
    val background: String = "minecraft:textures/block/lodestone_top.png",
)