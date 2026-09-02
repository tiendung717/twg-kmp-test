package nz.co.warehouseandroidtest.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.plugin
import io.ktor.client.request.header
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

private val REJECTED_TOKEN_STATUSES = setOf(
    HttpStatusCode.Unauthorized,
    HttpStatusCode.Forbidden
)

internal fun HttpClientConfig<*>.installDefaults(jsonParser: Json) {
    followRedirects = true
    install(ContentNegotiation) { json(jsonParser) }
    install(Logging) { level = LogLevel.ALL }
    install(DefaultRequest)
    defaultRequest {
        url(Api.BASE_URL)
        header(Api.HEADER_DEVICE, platformDevice)
        header(Api.HEADER_SUBSCRIPTION_KEY, Api.SUBSCRIPTION_KEY)
    }
}

internal fun HttpClient.installAuth(authenticator: Authenticator) {
    plugin(HttpSend).intercept { request ->
        val token = authenticator.currentToken() ?: authenticator.login()
        request.headers[Api.HEADER_TOKEN] = token

        val call = execute(request)
        if (call.response.status in REJECTED_TOKEN_STATUSES) {
            request.headers[Api.HEADER_TOKEN] = authenticator.refresh(token)
            execute(request)
        } else {
            return@intercept call
        }
    }
}
