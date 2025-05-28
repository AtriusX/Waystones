package xyz.atrius.waystones.data.json.advancement

class Criteria(
    vararg items: Pair<String, Trigger>,
) : Map<String, Trigger> by items.associate({ it })
