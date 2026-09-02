package nz.co.warehouseandroidtest.data.remote

import nz.co.warehouseandroidtest.data.local.TokenManager
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Single
class Authenticator(
    @Named(AUTH_CLIENT) private val authClient: HttpClient,
    private val tokenManager: TokenManager,
) {
    private val mutex = Mutex()

    suspend fun currentToken(): String? = tokenManager.read()

    suspend fun login(): String = mutex.withLock { authenticate() }

    suspend fun refresh(staleToken: String?): String = mutex.withLock {
        val stored = tokenManager.read()
        if (stored != null && stored != staleToken) stored else authenticate()
    }

    private suspend fun authenticate(): String {
        val response: HttpResponse = authClient.get(Api.LOGIN) {
            header(HttpHeaders.Authorization, Api.GUEST)
        }
        val token = response.headers[Api.HEADER_TOKEN]
            ?: error("Guest login returned ${response.status} without a ${Api.HEADER_TOKEN} header")

        tokenManager.save(token)
        return token
    }
}
