package xyz.atrius.waystones.data

import xyz.atrius.waystones.data.json.Json

enum class NodeType {
    WAYSTONE, BEACON
}

sealed class Node<T>(
    val type: NodeType
) : Json<T> {

    data class Waystone(
        val name: String? = null
    ) : Node<Waystone>(NodeType.WAYSTONE)

    class Beacon : Node<Beacon>(NodeType.BEACON)
}




