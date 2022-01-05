package xyz.atrius.waystones.data

import xyz.atrius.waystones.data.json.Json

sealed class Node<T>(
    val type: NodeType
) : Json<T> {

    enum class NodeType {
        WAYSTONE, BEACON
    }

    data class Waystone(
        val name: String? = null
    ) : Node<Waystone>(NodeType.WAYSTONE)

    @Suppress("unused") // Not currently in use
    object Beacon : Node<Beacon>(NodeType.BEACON)
}
