package nz.co.warehouseandroidtest.feature.product.domain.model

data class ProductDetail(
    val id: String,
    val name: String,
    val brand: String?,
    val description: String?,
    val imageUrls: List<String>,
    val price: Double?,
    val promotions: List<String> = emptyList(),
    val features: List<String> = emptyList(),
    val itemNumber: String? = null,
    val categories: List<String> = emptyList(),
    val isAvailable: Boolean = true,
    val stockOnHand: Int? = null,
)
