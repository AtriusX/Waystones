package xyz.atrius.waystones.data.json.advancement

data class Description(
    val text: String,
    val icon: Icon,
    val showToast: Boolean,
    val announceToChat: Boolean,
    val hidden: Boolean,
    val background: String,
)