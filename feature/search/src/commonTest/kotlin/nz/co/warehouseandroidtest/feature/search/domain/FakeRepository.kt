package nz.co.warehouseandroidtest.feature.search.domain

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import nz.co.warehouseandroidtest.data.ResultState
import nz.co.warehouseandroidtest.data.remote.model.SearchResponseDto
import nz.co.warehouseandroidtest.data.repository.Repository

/**
 * [Repository] is a concrete class, so it cannot be stubbed directly. This drives the real one over
 * a [MockEngine] instead, which also exercises the query parameters and JSON decoding for free.
 */
class FakeRepository(
    var searchResult: (term: String, start: Int, limit: Int) -> ResultState<SearchResponseDto> = { _, _, _ ->
        ResultState.Success(SearchResponseDto())
    },
) {
    val searchCalls = mutableListOf<Triple<String, Int, Int>>()

    private val json = Json { ignoreUnknownKeys = true }

    val repository: Repository = Repository(
        HttpClient(
            MockEngine { request ->
                val term = request.url.parameters["Search"].orEmpty()
                val start = request.url.parameters["Start"]?.toIntOrNull() ?: 0
                val limit = request.url.parameters["Limit"]?.toIntOrNull() ?: 0
                searchCalls += Triple(term, start, limit)

                when (val result = searchResult(term, start, limit)) {
                    is ResultState.Success -> respond(
                        content = json.encodeToString(result.data),
                        headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()),
                    )

                    is ResultState.Failure -> throw result.throwable
                    ResultState.Loading -> error("Unexpected loading state")
                }
            }
        ) {
            install(ContentNegotiation) { json(json) }
            defaultRequest { url("https://test.local/") }
        }
    )
}
