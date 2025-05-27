package xyz.atrius.waystones.data.json.advancement

data class Advancement(
    val display: Display,
    val criteria: Criteria = Criteria(
        "command" to Trigger()
    ),
    val parent: String? = null,
)