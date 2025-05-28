package xyz.atrius.waystones.utility

import arrow.core.Either

inline fun <A, B> Either<A, B>.foldResult(left: (A) -> B): B =
    fold(left) { it }
