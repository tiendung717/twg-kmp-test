package nz.co.warehouseandroidtest.feature.search.domain.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import nz.co.warehouseandroidtest.data.ResultState
import nz.co.warehouseandroidtest.data.repository.Repository
import nz.co.warehouseandroidtest.feature.search.domain.mapper.SearchMapper
import nz.co.warehouseandroidtest.feature.search.domain.model.Product

internal class SearchPagingSource(
    private val repo: Repository,
    private val mapper: SearchMapper,
    private val query: String,
) : PagingSource<Int, Product>() {

    private val mutex = Mutex()
    private val idsByKey = mutableMapOf<Int, List<String>>()

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        val start = params.key ?: 0

        return when (val result = repo.search(query, start, params.loadSize)) {
            is ResultState.Success -> {
                val page = mapper.toSearchResult(result.data, start)
                LoadResult.Page(
                    data = mutex.withLock { page.products.dropAlreadySeen(start) },
                    prevKey = null,
                    nextKey = if (page.hasMore) page.nextStart else null,
                )
            }

            is ResultState.Failure -> LoadResult.Error(result.throwable)

            ResultState.Loading -> LoadResult.Error(IllegalStateException("Unexpected loading state"))
        }
    }

    /**
     * Offset paging over a relevance-sorted feed can hand back a row an earlier page already
     * carried, and a repeated id kills the LazyGrid with "Key ... was already used". Recording per
     * load key rather than in one flat set keeps a retry of the same [start] idempotent.
     */
    private fun List<Product>.dropAlreadySeen(start: Int): List<Product> {
        val earlier = idsByKey.asSequence()
            .filter { it.key < start }
            .flatMap { it.value }
            .toSet()
        val kept = filterNot { it.id in earlier }
        idsByKey[start] = kept.map(Product::id)
        return kept
    }

    /** The feed is forward-only from offset 0, so a refresh restarts at the top. */
    override fun getRefreshKey(state: PagingState<Int, Product>): Int? = null
}
