package nz.co.warehouseandroidtest.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import nz.co.warehouseandroidtest.data.ResultState
import nz.co.warehouseandroidtest.data.safeApiCall
import nz.co.warehouseandroidtest.data.remote.Api
import nz.co.warehouseandroidtest.data.remote.LOGIN_CLIENT
import nz.co.warehouseandroidtest.data.remote.model.ProductResponseDto
import nz.co.warehouseandroidtest.data.remote.model.SearchResponseDto
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Single
class Repository(
    @Named(LOGIN_CLIENT) private val httpClient: HttpClient,
) {
    suspend fun search(term: String, start: Int, limit: Int): ResultState<SearchResponseDto> =
        safeApiCall {
            httpClient.get(Api.SEARCH) {
                parameter(Api.PARAM_SEARCH, term)
                parameter(Api.PARAM_START, start)
                parameter(Api.PARAM_LIMIT, limit)
            }.body()
        }

    suspend fun getProduct(productId: String): ResultState<ProductResponseDto> =
        safeApiCall {
            httpClient.get(Api.PRODUCT) {
                parameter(Api.PARAM_PRODUCT_ID, productId)
            }.body()
        }
}
