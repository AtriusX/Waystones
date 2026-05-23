package xyz.atrius.waystones.service

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensureNotNull
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single
import org.slf4j.LoggerFactory
import xyz.atrius.waystones.config.WebEndpoints
import xyz.atrius.waystones.data.CacheDelegate
import xyz.atrius.waystones.internal.KotlinPlugin
import java.sql.Timestamp
import kotlin.time.Duration.Companion.seconds

@Single
class PluginUpdateService(
    @Provided private val plugin: KotlinPlugin,
    private val webRequestService: WebRequestService,
) {

    private val cachedRelease by CacheDelegate(60.seconds) {
        check()
    }

    fun checkForUpdate(): Either<UpdateCheckError, Release> = cachedRelease

    private fun check(): Either<UpdateCheckError, Release> = either {
        val results = webRequestService
            .sendRequest(WebEndpoints.VERSION_ENDPOINT, type = ReleaseInfo::class)
            .mapLeft {
                logger.error("Failed to retrieve version info: ${it.message}")
                UpdateCheckError.UnableToRetrieve
            }
            .bind()
            .result
        val version = plugin.pluginMeta.version
        val index = results
            .indexOfFirst { it.name.startsWith(version) }
        // If the current plugin version is the first entry, then we
        if (index == 0) {
            raise(UpdateCheckError.NoUpdateAvailable)
        }
        // Ensure we can retrieve the latest version
        ensureNotNull(results.firstOrNull()) {
            logger.info("No versions returned for plugin, unable to check for updates...")
            UpdateCheckError.UnableToRetrieve
        }
    }

    sealed class UpdateCheckError {
        object UnableToRetrieve : UpdateCheckError()
        object NoUpdateAvailable : UpdateCheckError()
    }

    data class ReleaseInfo(
        val result: List<Release>,
    )

    data class Release(
        val id: Int,
        val projectId: Int,
        val name: String,
        val createdAt: Timestamp,
    )

    companion object {

        private val logger = LoggerFactory
            .getLogger(PluginUpdateService::class.java)
    }
}
