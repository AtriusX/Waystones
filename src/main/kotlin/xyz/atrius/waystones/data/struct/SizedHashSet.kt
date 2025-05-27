package xyz.atrius.waystones.data.struct

open class SizedHashSet<E>(private val maxSize: Int) : LinkedHashSet<E>() {

    override fun add(element: E): Boolean = when (size < maxSize) {
        true -> super.add(element)
        else -> false
    }
}