package xyz.atrius.waystones.data

import org.slf4j.LoggerFactory
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import kotlin.time.Duration

class CacheDelegate<T>(
    private val duration: Duration,
    private val cacheFun: () -> T,
) : ReadWriteProperty<Any?, T> {
    private var value: T? = null
    private var timestamp: Long = System.currentTimeMillis()

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        if (value == null || System.currentTimeMillis() > timestamp) {
            logger.info("Cache entry for ${property.name} has expired, updating...")
            store(cacheFun())
        }
        // This is a bit silly since we cache the value above before returning
        // this, but I'd rather avoid using !! in this context.
        return value ?: cacheFun()
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        store(value)
    }

    private fun store(value: T) {
        this.value = value
        timestamp = System.currentTimeMillis() + duration.inWholeMilliseconds
    }

    companion object {

        private val logger = LoggerFactory
            .getLogger(CacheDelegate::class.java.name)
    }
}
