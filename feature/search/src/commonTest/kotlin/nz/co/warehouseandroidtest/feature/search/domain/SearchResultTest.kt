package nz.co.warehouseandroidtest.feature.search.domain

import nz.co.warehouseandroidtest.feature.search.domain.model.Product
import nz.co.warehouseandroidtest.feature.search.domain.model.SearchResult

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SearchResultTest {

    private fun products(count: Int) = List(count) {
        Product(
            id = "R$it",
            name = "Item $it",
            description = null,
            imageUrl = null,
            price = null,
        )
    }

    @Test
    fun hasMoreWhileThePageEndsBeforeTheTotal() {
        assertTrue(SearchResult(products(20), total = 100, start = 0).hasMore)
        assertTrue(SearchResult(products(20), total = 41, start = 20).hasMore)
    }

    @Test
    fun hasNoMoreOnceThePageReachesTheTotal() {
        assertFalse(SearchResult(products(20), total = 40, start = 20).hasMore)
        assertFalse(SearchResult(products(5), total = 5, start = 0).hasMore)
    }

    @Test
    fun hasNoMoreForAnEmptyResult() {
        assertFalse(SearchResult(products(0), total = 0, start = 0).hasMore)
    }

    @Test
    fun emptyConstantIsAnEmptyFirstPage() {
        assertTrue(SearchResult.Empty.products.isEmpty())
        assertFalse(SearchResult.Empty.hasMore)
    }
}
