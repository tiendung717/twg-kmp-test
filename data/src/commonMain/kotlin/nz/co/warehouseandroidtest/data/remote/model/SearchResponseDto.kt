package nz.co.warehouseandroidtest.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResponseDto(
    @SerialName("products") val products: List<ProductDto> = emptyList(),
    @SerialName("searchTerm") val searchTerm: String? = null,
    @SerialName("total") val total: Int? = null,
    @SerialName("guest") val guest: Boolean? = null,
    @SerialName("apiVersion") val apiVersion: Double? = null,
)

@Serializable
data class ProductDto(
    @SerialName("productId") val productId: String? = null,
    @SerialName("productKey") val productKey: Long? = null,
    @SerialName("productName") val productName: String? = null,
    @SerialName("productDescription") val productDescription: String? = null,
    @SerialName("productImageUrl") val productImageUrl: String? = null,
    @SerialName("productBarcode") val productBarcode: String? = null,
    @SerialName("productSet") val productSet: Boolean? = null,
    @SerialName("brandCode") val brandCode: String? = null,
    @SerialName("brandDescription") val brandDescription: String? = null,
    @SerialName("priceInfo") val priceInfo: PriceInfoDto? = null,
    @SerialName("imageGroups") val imageGroups: List<ImageGroupDto> = emptyList(),
    @SerialName("inventory") val inventory: InventoryDto? = null,
    @SerialName("promotions") val promotions: List<PromotionDto> = emptyList(),
    @SerialName("productBadges") val productBadges: List<ProductBadgeDto> = emptyList(),
    @SerialName("categoryId") val categoryId: String? = null,
    @SerialName("secondaryCategoryIds") val secondaryCategoryIds: List<String> = emptyList(),
    @SerialName("shippingSize") val shippingSize: String? = null,
    @SerialName("isOversized") val isOversized: Boolean? = null,
    @SerialName("manufacturer") val manufacturer: String? = null,
    @SerialName("manufacturerSku") val manufacturerSku: String? = null,
    @SerialName("colourAttribute") val colourAttribute: String? = null,
    @SerialName("colourDescription") val colourDescription: String? = null,
    @SerialName("refinementColour") val refinementColour: String? = null,
    @SerialName("sizeAttribute") val sizeAttribute: String? = null,
    @SerialName("sizeDescription") val sizeDescription: String? = null,
    @SerialName("soldOnline") val soldOnline: String? = null,
    @SerialName("clickAndCollect") val clickAndCollect: String? = null,
    @SerialName("isClickAndCollect") val isClickAndCollect: Boolean? = null,
    @SerialName("isDigital") val isDigital: Boolean? = null,
    @SerialName("isGiftcard") val isGiftcard: Boolean? = null,
    @SerialName("isEssentialItem") val isEssentialItem: Boolean? = null,
    @SerialName("isMarketPlace") val isMarketPlace: Boolean? = null,
    @SerialName("subClassId") val subClassId: String? = null,
    @SerialName("deliveryTime") val deliveryTime: String? = null,
    @SerialName("mdmProductId") val mdmProductId: String? = null,
)

@Serializable
data class ProductBadgeDto(
    @SerialName("id") val id: String? = null,
    @SerialName("definition") val definition: BadgeDefinitionDto? = null,
)

@Serializable
data class BadgeDefinitionDto(
    @SerialName("position") val position: String? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("order") val order: Int? = null,
)
