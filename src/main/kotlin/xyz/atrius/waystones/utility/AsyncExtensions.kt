/**
 * Extension functions for bridging asynchronous operations to the Bukkit main thread.
 *
 * CompletableFuture callbacks execute on worker threads by default. These extensions
 * schedule the callback action on the Bukkit main thread once the future completes,
 * ensuring thread-safe interaction with the Bukkit API.
 */
package xyz.atrius.waystones.utility

import org.bukkit.Bukkit
import xyz.atrius.waystones.internal.KotlinPlugin
import java.util.concurrent.CompletableFuture
import arrow.core.Either

fun <T> CompletableFuture<T>.runOnMainThread(
    plugin: KotlinPlugin,
    action: (T) -> Unit
) {
    this.thenAccept { result ->
        Bukkit.getScheduler().runTask(plugin, Runnable {
            action(result)
        })
    }
}

fun <L, R> CompletableFuture<Either<L, R>>.runEitherOnMainThread(
    plugin: KotlinPlugin,
    onSuccess: (R) -> Unit,
    onFailure: (L) -> Unit
) {
    this.thenAccept { result ->
        Bukkit.getScheduler().runTask(plugin, Runnable {
            result.fold(onFailure, onSuccess)
        })
    }
}

fun KotlinPlugin.runOnMainThread(action: () -> Unit) {
    Bukkit.getScheduler().runTask(this, Runnable {
        action()
    })
}
