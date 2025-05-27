package xyz.atrius.waystones.data.struct

class StackSet<E>(initialData: List<E>) : HashMap<E, Int>() {

    init {
        initialData.forEach { add(it) }
    }

    fun add(data: E) {
        this[data] = getOrDefault(data, 0) + 1
    }

    override fun remove(key: E): Int? {
        this[key] = this[key]
            ?.minus(1)
            ?: 0

        return this[key]
    }
}