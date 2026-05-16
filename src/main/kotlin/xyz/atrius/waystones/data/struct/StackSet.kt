package xyz.atrius.waystones.data.struct

class StackSet<E>(initialData: List<E>) : HashMap<E, Int>() {

    init {
        initialData.forEach { add(it) }
    }

    fun add(data: E) {
        this[data] = getOrDefault(data, 0) + 1
    }

    override fun remove(key: E): Int? {
        val current = this[key] ?: return null
        return if (current <= 1) {
            super.remove(key)
            0
        } else {
            this[key] = current - 1
            current - 1
        }
    }
}
