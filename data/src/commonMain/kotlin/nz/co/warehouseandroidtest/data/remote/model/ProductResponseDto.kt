package nz.co.warehouseandroidtest.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductResponseDto(
    @SerialName("product") val product: ProductDetailDto? = null,
    @SerialName("guest") val guest: Boolean? = null,
    @SerialName("apiVersion") val apiVersion: Double? = null,
)

@Serializable
data class ProductDetailDto(
    @SerialName("productId") val productId: String? = null,
    @SerialName("productKey") val productKey: Long? = null,
    @SerialName("productName") val productName: String? = null,
    @SerialName("productDescription") val productDescription: String? = null,
    @SerialName("productBarcode") val productBarcode: String? = null,
    @SerialName("productUrl") val productUrl: String? = null,
    @SerialName("brandCode") val brandCode: String? = null,
    @SerialName("brandDescription") val brandDescription: String? = null,
    @SerialName("priceInfo") val priceInfo: PriceInfoDto? = null,
    @SerialName("onSpecial") val onSpecial: Boolean? = null,
    @SerialName("isClearance") val isClearance: Boolean? = null,
    @SerialName("imageUrls") val imageUrls: List<String> = emptyList(),
    @SerialName("imageGroups") val imageGroups: List<ImageGroupDto> = emptyList(),
    @SerialName("inventory") val inventory: InventoryDto? = null,
    @SerialName("promotions") val promotions: List<PromotionDto> = emptyList(),
    @SerialName("featureList") val featureList: List<String> = emptyList(),
    @SerialName("categoryId") val categoryId: String? = null,
    @SerialName("categoryHierarchy") val categoryHierarchy: List<CategoryDto> = emptyList(),
    @SerialName("secondaryCategoryIds") val secondaryCategoryIds: List<String> = emptyList(),
    @SerialName("secondaryCategoriesHierarchy") val secondaryCategoriesHierarchy: List<List<CategoryDto>> = emptyList(),
    @SerialName("hierarchy") val hierarchy: List<HierarchyLevelDto> = emptyList(),
    @SerialName("isMaster") val isMaster: Boolean? = null,
    @SerialName("isDangerousGoods") val isDangerousGoods: Boolean? = null,
    @SerialName("hasSizingChart") val hasSizingChart: Boolean? = null,
    @SerialName("clickAndCollectExcludedBranches") val clickAndCollectExcludedBranches: List<String> = emptyList(),
    @SerialName("shippingSize") val shippingSize: String? = null,
    @SerialName("isOversized") val isOversized: Boolean? = null,
    @SerialName("partPayRestricted") val partPayRestricted: String? = null,
    @SerialName("afterPayRestricted") val afterPayRestricted: String? = null,
    @SerialName("giftCardRestricted") val giftCardRestricted: String? = null,
    @SerialName("manufacturer") val manufacturer: String? = null,
    @SerialName("manufacturerSku") val manufacturerSku: String? = null,
    @SerialName("colourAttribute") val colourAttribute: String? = null,
    @SerialName("colourDescription") val colourDescription: String? = null,
    @SerialName("sizeAttribute") val sizeAttribute: String? = null,
    @SerialName("sizeDescription") val sizeDescription: String? = null,
    @SerialName("soldOnline") val soldOnline: String? = null,
    @SerialName("clickAndCollect") val clickAndCollect: String? = null,
    @SerialName("isClickAndCollect") val isClickAndCollect: Boolean? = null,
    @SerialName("isDigital") val isDigital: Boolean? = null,
    @SerialName("isGiftcard") val isGiftcard: Boolean? = null,
    @SerialName("isEssentialItem") val isEssentialItem: Boolean? = null,
    @SerialName("subClassId") val subClassId: String? = null,
    @SerialName("deliveryTime") val deliveryTime: String? = null,
    @SerialName("isMarketPlace") val isMarketPlace: Boolean? = null,
    @SerialName("mdmProductId") val mdmProductId: String? = null,
)

@Serializable
data class CategoryDto(
    @SerialName("categoryId") val categoryId: String? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("parentCategoryId") val parentCategoryId: String? = null,
    @SerialName("parentCategoryName") val parentCategoryName: String? = null,
    @SerialName("productCount") val productCount: Int? = null,
    @SerialName("subCategoryCount") val subCategoryCount: Int? = null,
    @SerialName("showInBrowse") val showInBrowse: Boolean? = null,
    @SerialName("excludeFromVisualBrowse") val excludeFromVisualBrowse: Boolean? = null,
)

@Serializable
data class HierarchyLevelDto(
    @SerialName("level") val level: Int? = null,
    @SerialName("code") val code: String? = null,
    @SerialName("description") val description: String? = null,
)
