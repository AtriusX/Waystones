package xyz.atrius.waystones.service

import arrow.core.Either
import com.google.gson.Gson
import org.koin.core.annotation.Single
import org.slf4j.LoggerFactory
import xyz.atrius.waystones.data.HttpStatus
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import kotlin.reflect.KClass

@Single
class WebRequestService(
    private val gson: Gson,
) {

    fun <T : Any> sendRequest(
        endpoint: String,
        method: HttpStatus = HttpStatus.GET,
        type: KClass<T>,
    ): Either<Throwable, T> {
        val client = HttpClient.newHttpClient()
        val result = Either
            .catch {
                val request = HttpRequest.newBuilder()
                    .uri(URI.create(endpoint))
                    .method(method.name, HttpRequest.BodyPublishers.noBody())
                    .build()

                logger.debug("Firing off request at endpoint: {} {}", method, endpoint)

                val response = client
                    .send(request, HttpResponse.BodyHandlers.ofString())

                gson.fromJson(response.body(), type.java)
            }
            .onLeft {
                logger.error("Error while sending request: $method $endpoint", it)
            }

        client.close()
        return result
    }

    companion object {

        private val logger = LoggerFactory
            .getLogger(WebRequestService::class.java.name)
    }
}
