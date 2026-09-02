package nz.co.warehouseandroidtest.feature.search.domain.model

data class SearchResult(
    val products: List<Product>,
    val total: Int,
    val start: Int,
    val received: Int = products.size,
) {
    val nextStart: Int
        get() = start + received

    val hasMore: Boolean
        get() = received > 0 && nextStart < total

    companion object {
        val Empty = SearchResult(products = emptyList(), total = 0, start = 0)
    }
}
