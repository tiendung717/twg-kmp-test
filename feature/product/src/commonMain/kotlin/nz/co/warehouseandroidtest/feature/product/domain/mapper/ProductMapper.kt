package nz.co.warehouseandroidtest.feature.product.domain.mapper

import nz.co.warehouseandroidtest.data.remote.model.ProductDetailDto
import nz.co.warehouseandroidtest.data.remote.model.ProductResponseDto
import nz.co.warehouseandroidtest.data.remote.model.PromotionDto
import nz.co.warehouseandroidtest.feature.product.domain.model.ProductDetail
import org.koin.core.annotation.Single

@Single
class ProductMapper {

    fun toProductDetail(dto: ProductResponseDto): ProductDetail? = dto.product?.let(::toProductDetail)

    fun toProductDetail(dto: ProductDetailDto): ProductDetail? {
        val id = dto.productId?.takeIf { it.isNotBlank() } ?: return null
        return ProductDetail(
            id = id,
            name = dto.productName.orEmpty(),
            brand = dto.brandDescription?.takeIf { it.isNotBlank() },
            description = dto.productDescription?.takeIf { it.isNotBlank() },
            imageUrls = dto.allImageUrls(),
            price = dto.priceInfo?.price,
            promotions = dto.promotions.customerFacing(),
            features = dto.featureList.mapNotNull { it.takeIf(String::isNotBlank) },
            itemNumber = dto.productKey?.toString(),
            categories = dto.categoryNames(),
            isAvailable = dto.inventory?.available ?: true,
            stockOnHand = dto.inventory?.soh,
        )
    }

    private fun ProductDetailDto.allImageUrls(): List<String> =
        imageUrls.filter { it.isNotBlank() }
            .ifEmpty { imageGroups.flatMap { it.imageUrls }.filter { it.isNotBlank() } }
            .distinct()

    private fun ProductDetailDto.categoryNames(): List<String> =
        categoryHierarchy.mapNotNull { it.name?.takeIf(String::isNotBlank) }.distinct()

    private fun List<PromotionDto>.customerFacing(): List<String> =
        filterNot { PLUMBING_TAG in it.tags }
            .mapNotNull { promotion ->
                promotion.description?.takeIf { it.isNotBlank() }
                    ?: promotion.dealDescription?.takeIf { it.isNotBlank() }
            }
            .distinct()

    private companion object {
        const val PLUMBING_TAG = "ExcludeFromRefinement"
    }
}
