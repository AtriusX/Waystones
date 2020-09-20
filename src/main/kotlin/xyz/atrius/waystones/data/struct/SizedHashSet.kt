package xyz.atrius.waystones.data.struct

open class SizedHashSet<E>(private val maxSize: Int) : LinkedHashSet<E>() {
    override fun add(element: E): Boolean = if (size < maxSize) super.add(element) else false
}