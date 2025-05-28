package xyz.atrius.waystones.utility

import kotlin.math.sqrt as ktSqrt

// Helper function since kotlin doesn't have a native fixed-point square root
fun sqrt(value: Int): Int =
    ktSqrt(value.toFloat()).toInt()

// Calculates the character representation of an object based on its hashcode
fun Any.hashChar(): Char =
    hashCode().toChar()
