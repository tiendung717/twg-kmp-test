package nz.co.warehouseandroidtest.feature.search.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import nz.co.warehouseandroidtest.feature.search.domain.mapper.SearchMapper
import nz.co.warehouseandroidtest.feature.search.domain.model.Product
import nz.co.warehouseandroidtest.feature.search.domain.paging.SearchPagingSource
import kotlinx.coroutines.flow.Flow
import nz.co.warehouseandroidtest.data.repository.Repository
import org.koin.core.annotation.Single

@Single
class SearchUseCase(
    private val repo: Repository,
    private val mapper: SearchMapper,
) {
    operator fun invoke(term: String): Flow<PagingData<Product>> = Pager(
        config = PagingConfig(
            pageSize = DEFAULT_PAGE_SIZE,
            initialLoadSize = DEFAULT_PAGE_SIZE,
            enablePlaceholders = false,
        ),
        pagingSourceFactory = { SearchPagingSource(repo, mapper, term) },
    ).flow

    companion object {
        const val DEFAULT_PAGE_SIZE = 20
    }
}
