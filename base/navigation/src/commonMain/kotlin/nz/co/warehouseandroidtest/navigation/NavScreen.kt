package nz.co.warehouseandroidtest.navigation

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
sealed class NavScreen {

    @Serializable
    data object Search: NavScreen()

    @Serializable
    data class Product(val productId: String): NavScreen()

}
