package nz.co.warehouseandroidtest.data.remote

internal object Api {
    const val BASE_URL = "https://legacy-apim.twg.co.nz/twgCSharpTest/"

    const val LOGIN = "Login.json"
    const val SEARCH = "Search.json"
    const val PRODUCT = "Product.json"

    const val PARAM_SEARCH = "Search"
    const val PARAM_START = "Start"
    const val PARAM_LIMIT = "Limit"
    const val PARAM_PRODUCT_ID = "ProductId"

    const val HEADER_DEVICE = "X-TWL-Device"
    const val HEADER_TOKEN = "X-TWL-Token"
    const val HEADER_SUBSCRIPTION_KEY = "Ocp-Apim-Subscription-Key"

    const val GUEST = "Guest"

    const val SUBSCRIPTION_KEY = "a2c39ac0c6ee41e382c111802dac390c"
}

internal expect val platformDevice: String
