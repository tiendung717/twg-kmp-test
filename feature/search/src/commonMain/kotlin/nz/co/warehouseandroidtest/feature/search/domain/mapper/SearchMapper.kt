package nz.co.warehouseandroidtest.feature.search.domain.mapper

import nz.co.warehouseandroidtest.data.remote.model.ProductDto
import nz.co.warehouseandroidtest.data.remote.model.SearchResponseDto
import nz.co.warehouseandroidtest.feature.search.domain.model.Product
import nz.co.warehouseandroidtest.feature.search.domain.model.SearchResult
import org.koin.core.annotation.Single

@Single
class SearchMapper {

    fun toSearchResult(dto: SearchResponseDto, start: Int): SearchResult {
        val products = dto.products.mapNotNull(::toProduct)
        return SearchResult(
            products = products,
            total = dto.total ?: products.size,
            start = start,
            received = dto.products.size,
        )
    }

    fun toProduct(dto: ProductDto): Product? {
        val id = dto.productId?.takeIf { it.isNotBlank() } ?: return null
        return Product(
            id = id,
            name = dto.productName.orEmpty(),
            description = dto.productDescription?.takeIf { it.isNotBlank() },
            imageUrl = dto.productImageUrl?.takeIf { it.isNotBlank() } ?: dto.firstGroupImage(),
            price = dto.priceInfo?.price,
        )
    }

    private fun ProductDto.firstGroupImage(): String? =
        imageGroups.firstNotNullOfOrNull { group ->
            group.imageUrls.firstOrNull { it.isNotBlank() }
        }
}
