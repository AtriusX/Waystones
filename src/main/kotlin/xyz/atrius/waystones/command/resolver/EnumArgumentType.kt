package xyz.atrius.waystones.command.resolver

import kotlin.reflect.KClass


class EnumArgumentType<T : Enum<*>>(
    type: KClass<T>,
) : LimitedArgumentType<T>(
    type.java.enumConstants
)