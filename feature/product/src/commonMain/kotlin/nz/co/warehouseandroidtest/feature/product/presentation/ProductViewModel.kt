package nz.co.warehouseandroidtest.feature.product.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import nz.co.warehouseandroidtest.data.ResultState
import nz.co.warehouseandroidtest.feature.product.domain.model.ProductDetail
import nz.co.warehouseandroidtest.feature.product.domain.usecase.ProductUseCase
import nz.co.warehouseandroidtest.navigation.NavScreen

data class ProductUiState(
    val product: ProductDetail? = null,
    val isLoading: Boolean = false,
    val error: Throwable? = null,
)

@KoinViewModel
class ProductViewModel(
    savedStateHandle: SavedStateHandle,
    private val getProductDetail: ProductUseCase,
) : ViewModel() {

    private val productId: String = savedStateHandle.toRoute<NavScreen.Product>().productId

    private val _state = MutableStateFlow(ProductUiState())
    val state = _state
        .onStart { fetch() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = ProductUiState()
        )

    fun retry() {
        fetch()
    }

    private fun fetch() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            when (val result = getProductDetail(productId)) {
                is ResultState.Success -> _state.update {
                    it.copy(product = result.data, isLoading = false)
                }

                is ResultState.Failure -> _state.update {
                    it.copy(
                        product = null,
                        isLoading = false,
                        error = result.throwable,
                    )
                }

                ResultState.Loading -> Unit
            }
        }
    }
}
