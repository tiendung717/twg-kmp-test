package nz.co.warehouseandroidtest.feature.product.domain

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
import nz.co.warehouseandroidtest.data.remote.model.ProductResponseDto
import nz.co.warehouseandroidtest.data.repository.Repository

class FakeRepository(
    var productResult: (productId: String) -> ResultState<ProductResponseDto> = {
        ResultState.Success(ProductResponseDto())
    },
) {
    val productCalls = mutableListOf<String>()

    private val json = Json { ignoreUnknownKeys = true }

    val repository: Repository = Repository(
        HttpClient(
            MockEngine { request ->
                val productId = request.url.parameters["ProductId"].orEmpty()
                productCalls += productId

                when (val result = productResult(productId)) {
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
