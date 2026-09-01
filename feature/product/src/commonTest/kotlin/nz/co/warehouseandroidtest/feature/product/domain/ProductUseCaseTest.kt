package nz.co.warehouseandroidtest.feature.product.domain

import kotlinx.coroutines.test.runTest
import nz.co.warehouseandroidtest.data.ResultState
import nz.co.warehouseandroidtest.data.remote.model.PriceInfoDto
import nz.co.warehouseandroidtest.data.remote.model.ProductDetailDto
import nz.co.warehouseandroidtest.data.remote.model.ProductResponseDto
import nz.co.warehouseandroidtest.feature.product.domain.mapper.ProductMapper
import nz.co.warehouseandroidtest.feature.product.domain.model.ProductDetail
import nz.co.warehouseandroidtest.feature.product.domain.usecase.ProductUseCase
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class ProductUseCaseTest {

    private val repo = FakeRepository()
    private val useCase = ProductUseCase(repo.repository, ProductMapper())

    @Test
    fun mapsASuccessfulResponseToTheDomainModel() = runTest {
        repo.productResult = {
            ResultState.Success(
                ProductResponseDto(
                    ProductDetailDto(
                        productId = "R1",
                        productName = "Olive Oil",
                        priceInfo = PriceInfoDto(19.99),
                    ),
                )
            )
        }

        val result = useCase("R1")

        val product = assertIs<ResultState.Success<ProductDetail>>(result).data
        assertEquals("R1", product.id)
        assertEquals("Olive Oil", product.name)
        assertEquals(19.99, product.price)
    }

    @Test
    fun passesTheRequestedIdToTheRepository() = runTest {
        useCase("R42")

        assertEquals(listOf("R42"), repo.productCalls)
    }

    @Test
    fun failsWhenTheResponseCarriesNoProduct() = runTest {
        repo.productResult = { ResultState.Success(ProductResponseDto(product = null)) }

        val failure = assertIs<ResultState.Failure>(useCase("R1"))

        assertIs<NoSuchElementException>(failure.throwable)
    }

    @Test
    fun failsWhenTheProductHasNoId() = runTest {
        repo.productResult = {
            ResultState.Success(ProductResponseDto(ProductDetailDto(productId = null, productName = "Ghost")))
        }

        assertIs<ResultState.Failure>(useCase("R1"))
    }

    @Test
    fun propagatesARepositoryFailureUnchanged() = runTest {
        val boom = IllegalStateException("network down")
        repo.productResult = { ResultState.Failure(boom) }

        val failure = assertIs<ResultState.Failure>(useCase("R1"))

        // Ktor re-creates the exception as it crosses the client pipeline, so identity no longer
        // holds; the type and message must still survive untouched.
        assertIs<IllegalStateException>(failure.throwable)
        assertEquals(boom.message, failure.throwable.message, "the original cause must not be swallowed")
    }
}
