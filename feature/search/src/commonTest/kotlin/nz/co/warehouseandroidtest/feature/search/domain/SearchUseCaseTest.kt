package nz.co.warehouseandroidtest.feature.search.domain

import androidx.paging.testing.asSnapshot
import kotlinx.coroutines.test.runTest
import nz.co.warehouseandroidtest.data.ResultState
import nz.co.warehouseandroidtest.data.remote.model.PriceInfoDto
import nz.co.warehouseandroidtest.data.remote.model.ProductDto
import nz.co.warehouseandroidtest.data.remote.model.SearchResponseDto
import nz.co.warehouseandroidtest.feature.search.domain.mapper.SearchMapper
import nz.co.warehouseandroidtest.feature.search.domain.usecase.SearchUseCase
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SearchUseCaseTest {

    private val repo = FakeRepository()
    private val useCase = SearchUseCase(repo.repository, SearchMapper())

    /** Serves [total] products, honouring the requested offset and limit. */
    private fun servePages(total: Int) {
        repo.searchResult = { _, start, limit ->
            val ids = (start until minOf(start + limit, total)).map { "R$it" }
            ResultState.Success(
                SearchResponseDto(
                    products = ids.map {
                        ProductDto(productId = it, productName = "Item $it", priceInfo = PriceInfoDto(9.99))
                    },
                    total = total,
                )
            )
        }
    }

    @Test
    fun emitsMappedDomainProducts() = runTest {
        servePages(total = 3)

        val products = useCase("jacket").asSnapshot()

        assertEquals(listOf("R0", "R1", "R2"), products.map { it.id })
        assertEquals("Item R0", products.first().name)
        assertEquals(9.99, products.first().price)
    }

    @Test
    fun requestsTheFirstPageFromOffsetZeroWithTheConfiguredPageSize() = runTest {
        servePages(total = 1)

        useCase("jacket").asSnapshot()

        val (term, start, limit) = repo.searchCalls.first()
        assertEquals("jacket", term)
        assertEquals(0, start)
        assertEquals(
            SearchUseCase.DEFAULT_PAGE_SIZE,
            limit,
            "initialLoadSize must match pageSize so Start/Limit stay aligned with the API",
        )
    }

    @Test
    fun scrollingPagesThroughToTheTotalWithoutDuplicates() = runTest {
        val total = 45
        servePages(total = total)

        val products = useCase("jacket").asSnapshot { scrollTo(total - 1) }

        assertEquals(total, products.size)
        assertEquals(total, products.map { it.id }.distinct().size, "no duplicates across pages")
        assertEquals("R44", products.last().id)
    }

    @Test
    fun surfacesARepositoryFailureToTheCollector() = runTest {
        val boom = IllegalStateException("network down")
        repo.searchResult = { _, _, _ -> ResultState.Failure(boom) }

        assertFailsWith<IllegalStateException> { useCase("jacket").asSnapshot() }
    }

    @Test
    fun dropsProductsWithoutAnId() = runTest {
        repo.searchResult = { _, start, _ ->
            val products = if (start == 0) {
                listOf(
                    ProductDto(productId = "R1", productName = "Keep"),
                    ProductDto(productId = null, productName = "Drop"),
                )
            } else {
                emptyList()
            }
            ResultState.Success(SearchResponseDto(products = products, total = 2))
        }

        assertEquals(listOf("R1"), useCase("jacket").asSnapshot().map { it.id })
    }

    @Test
    fun neverEmitsTheSameProductTwiceWhenTheFeedOverlapsPageBoundaries() = runTest {
        val total = 45
        // Reproduces the crash: an offset feed that hands back one row the previous page already had.
        repo.searchResult = { _, start, limit ->
            val from = (start - 1).coerceAtLeast(0)
            val ids = (from until minOf(from + limit, total)).map { "R$it" }
            ResultState.Success(
                SearchResponseDto(
                    products = ids.map {
                        ProductDto(productId = it, productName = "Item $it", priceInfo = PriceInfoDto(9.99))
                    },
                    total = total,
                )
            )
        }

        val products = useCase("jacket").asSnapshot { scrollTo(total - 1) }
        val ids = products.map { it.id }

        assertEquals(ids.distinct(), ids, "a repeated key crashes LazyGrid with \"Key ... was already used\"")
    }

    @Test
    fun advancesTheOffsetByWhatTheServerSentNotByWhatSurvivedMapping() = runTest {
        // Every page carries one unusable row; the offset must still step by the full page.
        repo.searchResult = { _, start, limit ->
            val ids = (start until minOf(start + limit - 1, 40)).map { "R$it" }
            ResultState.Success(
                SearchResponseDto(
                    products = ids.map { ProductDto(productId = it, productName = "Item $it") } +
                        ProductDto(productId = null, productName = "Unusable"),
                    total = 40,
                )
            )
        }

        useCase("jacket").asSnapshot { scrollTo(30) }

        val offsets = repo.searchCalls.map { it.second }
        assertEquals(offsets.distinct(), offsets, "a shrinking stride re-reads rows already shown")
        assertEquals(listOf(0, 20, 40).take(offsets.size), offsets)
    }
}
