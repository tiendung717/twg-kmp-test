package nz.co.warehouseandroidtest.feature.product.domain.usecase

import nz.co.warehouseandroidtest.data.ResultState
import nz.co.warehouseandroidtest.data.repository.Repository
import nz.co.warehouseandroidtest.feature.product.domain.mapper.ProductMapper
import nz.co.warehouseandroidtest.feature.product.domain.model.ProductDetail
import org.koin.core.annotation.Single

@Single
class ProductUseCase(
    private val repo: Repository,
    private val mapper: ProductMapper,
) {
    suspend operator fun invoke(productId: String): ResultState<ProductDetail> =
        when (val result = repo.getProduct(productId)) {
            is ResultState.Success -> mapper.toProductDetail(result.data)
                ?.let { ResultState.Success(it) }
                ?: ResultState.Failure(NoSuchElementException("No product found for id $productId"))

            is ResultState.Failure -> result
            ResultState.Loading -> ResultState.Loading
        }
}
