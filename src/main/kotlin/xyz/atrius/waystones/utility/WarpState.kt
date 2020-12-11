package xyz.atrius.waystones.utility

import xyz.atrius.waystones.configuration

sealed class WarpState(protected var message: String?) {

    object None : WarpState(null)

    object Unpowered : WarpState("%s does not currently have power")

    object Inhibited : WarpState(null)

    object Infinite : WarpState(null)

    object Obstructed : WarpState("%s is obstructed and cannot be used")

    open class Active(val range: Int) : WarpState(null) {
        override fun postCondition(): Boolean =
                configuration.limitDistance
    }

    class InterDimension(range: Int) : Active(range) {
        init {
            message = "Cannot locate %s due to dimensional interference"
        }

        override fun postCondition(): Boolean =
                !configuration.jumpWorlds
    }

    fun message(name: String) = if (postCondition()) message?.format(name) else null

    protected open fun postCondition(): Boolean = true
}

