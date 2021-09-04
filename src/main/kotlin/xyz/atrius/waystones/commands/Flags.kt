package xyz.atrius.waystones.commands

class Flags(
    values: Set<String>
) : Set<String> by (values.map {
    it.toLowerCase().removePrefix("-")
}.toSet()) {

    operator fun contains(values: Collection<String>): Boolean =
        values.map(String::toLowerCase).any(::contains)
}