package nz.co.warehouseandroidtest.feature.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import org.koin.android.annotation.KoinViewModel
import nz.co.warehouseandroidtest.feature.search.domain.model.Product
import nz.co.warehouseandroidtest.feature.search.domain.usecase.SearchUseCase

@KoinViewModel
class SearchViewModel(
    private val searchProducts: SearchUseCase,
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val products: Flow<PagingData<Product>> = _query
        .debounce { if (it.isBlank()) 0L else SEARCH_DEBOUNCE_MILLIS }
        .distinctUntilChanged()
        .flatMapLatest { term ->
            if (term.isBlank()) flowOf(PagingData.empty()) else searchProducts(term)
        }
        .cachedIn(viewModelScope)

    fun onQueryChange(query: String) {
        _query.value = query
    }

    private companion object {
        const val SEARCH_DEBOUNCE_MILLIS = 350L
    }
}
