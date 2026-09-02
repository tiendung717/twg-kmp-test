package nz.co.warehouseandroidtest.feature.search.domain.model

data class Product(
    val id: String,
    val name: String,
    val description: String?,
    val imageUrl: String?,
    val price: Double?,
)
