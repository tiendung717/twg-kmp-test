package nz.co.warehouseandroidtest.feature.product.domain

import nz.co.warehouseandroidtest.data.remote.model.ProductResponseDto
import nz.co.warehouseandroidtest.feature.product.domain.mapper.ProductMapper
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ProductMapperTest {

    private val json = Json { ignoreUnknownKeys = true }
    private val mapper = ProductMapper()

    private val productSample = """
        {
          "product": {
            "isMaster": false,
            "onSpecial": false,
            "imageUrls": [
              "https://beta.test/R2767564_30.jpg",
              "https://beta.test/R2767564_31.jpg"
            ],
            "productName": "Brother Inkvestment Tank MFC-J4340DWXL Printer",
            "priceInfo": { "price": 449.95 },
            "productKey": 2767564,
            "inventory": { "available": true, "preorderable": false, "backorderable": false, "soh": 16 },
            "productBarcode": "4977766809580",
            "promotions": [
              {
                "promotionId": "marketclub-sandbox-5off",
                "dealDescription": "5% off your order. Download our app and join MarketClub. This is the callout",
                "demandwareConditionsText": "This is the extra detail",
                "price": 449.95,
                "isMarketClubExclusive": false,
                "description": "5% off your order. Download our app and join MarketClub",
                "tags": ["ExcludeFromRefinement"]
              },
              {
                "promotionId": "twl-omni-Promo",
                "dealDescription": "Omni Products",
                "isMarketClubExclusive": false,
                "description": "Omni Products Promo",
                "tags": ["ExcludeFromRefinement"]
              }
            ],
            "brandCode": "BROTHER",
            "brandDescription": "Brother",
            "imageGroups": [
              { "colourAttribute": "", "imageUrls": ["https://okapi.test/R2767564_30.jpg"] }
            ],
            "productUrl": "https://www.thewarehouse.co.nz/s/twl/product/R2767564.html",
            "isDangerousGoods": false,
            "isClearance": false,
            "hasSizingChart": false,
            "compareSpecList": [],
            "clickAndCollectExcludedBranches": ["110", "192"],
            "productId": "R2767564",
            "categoryId": "electronicsgaming-computerstablets-printersscanners-inkjetprinters",
            "categoryHierarchy": [
              {
                "categoryId": "electronicsgaming-computerstablets-printersscanners-inkjetprinters",
                "parentCategoryId": "electronicsgaming-computerstablets-printersscanners",
                "parentCategoryName": "Printers & Scanners",
                "name": "Inkjet Printers",
                "description": "Shop Inkjet Printers at The Warehouse.",
                "sizeChartId": null,
                "productCount": 43,
                "subCategoryCount": 0,
                "excludeFromVisualBrowse": false
              },
              {
                "categoryId": "electronicsgaming",
                "name": "Electronics & Gaming",
                "description": "Browse the great range of Electronics & Gaming products here.",
                "sizeChartId": null,
                "productCount": 5609,
                "subCategoryCount": 19,
                "showInBrowse": true,
                "excludeFromVisualBrowse": false
              }
            ],
            "secondaryCategoryIds": ["electronicsgaming-computerstablets-printersscanners"],
            "secondaryCategoriesHierarchy": [[], [{ "categoryId": "electronicsgaming", "name": "Electronics & Gaming" }]],
            "productDescription": "The stylish MFC-J4340DW XL is a business-quality multifunction device.",
            "shippingSize": "Standard",
            "isOversized": false,
            "partPayRestricted": "N",
            "afterPayRestricted": "N",
            "giftCardRestricted": "N",
            "manufacturer": "1",
            "manufacturerSku": "MFCJ4340DWXL",
            "colourAttribute": "WHT",
            "colourDescription": "White",
            "soldOnline": "Y",
            "clickAndCollect": "O",
            "isClickAndCollect": true,
            "subClassId": "4460",
            "deliveryTime": "",
            "featureList": [
              "Print up to A4",
              "Wireless connectivity",
              "Consumables: LC436, LC436XL"
            ],
            "hierarchy": [
              { "level": 1, "code": "05783", "description": "Print & Consumables" },
              { "level": 4, "code": "4460", "description": "Brother Inkjet Printers" }
            ],
            "isMarketPlace": false,
            "mdmProductId": "455259"
          },
          "expiresDatetime": "2026-09-01T22:39:54Z",
          "guest": true,
          "apiVersion": 4.9
        }
    """.trimIndent()

    private fun detail() = mapper.toProductDetail(json.decodeFromString<ProductResponseDto>(productSample))

    @Test
    fun mapsTheRealProductPayloadToDomain() {
        val detail = requireNotNull(detail())

        assertEquals("R2767564", detail.id)
        assertEquals("Brother Inkvestment Tank MFC-J4340DWXL Printer", detail.name)
        assertEquals("Brother", detail.brand, "the display brand is brandDescription, not brandCode")
        assertEquals(449.95, detail.price, "price is nested under priceInfo")
        assertEquals("The stylish MFC-J4340DW XL is a business-quality multifunction device.", detail.description)
        assertTrue(detail.isAvailable)
    }

    @Test
    fun readsFeaturesFromFeatureList() {
        assertEquals(
            listOf("Print up to A4", "Wireless connectivity", "Consumables: LC436, LC436XL"),
            requireNotNull(detail()).features,
        )
    }

    @Test
    fun takesTheItemNumberFromProductKey() {
        assertEquals("2767564", requireNotNull(detail()).itemNumber)
    }

    @Test
    fun readsCategoryNamesFromTheHierarchy() {
        assertEquals(
            listOf("Inkjet Printers", "Electronics & Gaming"),
            requireNotNull(detail()).categories,
            "deepest first, as the feed orders them",
        )
    }

    @Test
    fun prefersTopLevelImageUrlsOverTheCdnGroups() {
        assertEquals(
            listOf("https://beta.test/R2767564_30.jpg", "https://beta.test/R2767564_31.jpg"),
            requireNotNull(detail()).imageUrls,
            "imageGroups repeat the same shots from another host, so they are a fallback only",
        )
    }

    @Test
    fun fallsBackToImageGroupsWhenImageUrlsIsAbsent() {
        val detail = mapper.toProductDetail(
            json.decodeFromString<ProductResponseDto>(
                """{"product":{"productId":"R1","imageGroups":[{"imageUrls":["https://okapi.test/a.jpg"]}]}}"""
            )
        )

        assertEquals(listOf("https://okapi.test/a.jpg"), requireNotNull(detail).imageUrls)
    }

    @Test
    fun keepsOnlyShopperFacingPromotions() {
        assertTrue(
            requireNotNull(detail()).promotions.isEmpty(),
            "every promotion on this product is tagged ExcludeFromRefinement, so none reach the UI",
        )
    }

    @Test
    fun prefersTheDescriptionOverTheDealDescription() {
        val detail = mapper.toProductDetail(
            json.decodeFromString<ProductResponseDto>(
                """
                {"product":{"productId":"R1","promotions":[
                  {"description":"Buy 1 get 1 HALF PRICE","dealDescription":"Buy 1 get 1 HALF PRICE Office Chairs s","tags":["badge_bogo50off"]}
                ]}}
                """.trimIndent()
            )
        )

        assertEquals(listOf("Buy 1 get 1 HALF PRICE"), requireNotNull(detail).promotions)
    }

    @Test
    fun returnsNullWhenTheProductIsAbsentOrHasNoId() {
        assertNull(mapper.toProductDetail(json.decodeFromString<ProductResponseDto>("""{}""")))
        assertNull(
            mapper.toProductDetail(
                json.decodeFromString<ProductResponseDto>("""{"product":{"productName":"Ghost"}}""")
            )
        )
    }

    @Test
    fun missingOptionalProductFieldsDegradeInsteadOfThrowing() {
        val detail = requireNotNull(
            mapper.toProductDetail(json.decodeFromString<ProductResponseDto>("""{"product":{"productId":"R1"}}"""))
        )

        assertEquals("", detail.name)
        assertNull(detail.brand)
        assertNull(detail.description)
        assertNull(detail.itemNumber)
        assertTrue(detail.imageUrls.isEmpty())
        assertTrue(detail.promotions.isEmpty())
        assertTrue(detail.features.isEmpty())
        assertTrue(detail.categories.isEmpty())
        assertTrue(detail.isAvailable, "an absent inventory block is treated as available")
    }

    @Test
    fun readsStockOnHandFromInventory() {
        assertEquals(16, requireNotNull(detail()).stockOnHand)
    }



    @Test
    fun reportsSoldOutWhenInventorySaysSo() {
        val detail = mapper.toProductDetail(
            json.decodeFromString<ProductResponseDto>(
                """{"product":{"productId":"R1","inventory":{"available":false,"soh":0}}}"""
            )
        )

        assertFalse(requireNotNull(detail).isAvailable)
        assertEquals(0, detail.stockOnHand)
    }
}
