package nz.co.warehouseandroidtest.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PriceInfoDto(
    @SerialName("price") val price: Double? = null,
)

@Serializable
data class ImageGroupDto(
    @SerialName("colourAttribute") val colourAttribute: String? = null,
    @SerialName("imageUrls") val imageUrls: List<String> = emptyList(),
)

@Serializable
data class InventoryDto(
    @SerialName("available") val available: Boolean? = null,
    @SerialName("preorderable") val preorderable: Boolean? = null,
    @SerialName("backorderable") val backorderable: Boolean? = null,
    @SerialName("soh") val soh: Int? = null,
)

@Serializable
data class PromotionDto(
    @SerialName("promotionId") val promotionId: String? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("dealDescription") val dealDescription: String? = null,
    @SerialName("demandwareConditionsText") val conditionsText: String? = null,
    @SerialName("price") val price: Double? = null,
    @SerialName("isMarketClubExclusive") val isMarketClubExclusive: Boolean? = null,
    @SerialName("marketClubExclusiveMessage") val marketClubExclusiveMessage: String? = null,
    @SerialName("tags") val tags: List<String> = emptyList(),
)
