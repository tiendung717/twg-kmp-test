package nz.co.warehouseandroidtest.feature.search.domain

import nz.co.warehouseandroidtest.data.remote.model.SearchResponseDto
import nz.co.warehouseandroidtest.feature.search.domain.mapper.SearchMapper
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SearchMapperTest {

    private val json = Json { ignoreUnknownKeys = true }
    private val mapper = SearchMapper()

    /** Trimmed verbatim from a real GET /search?Search=j response. */
    private val searchSample = """
        {
          "products": [
            {
              "productName": "Jasper J Loop Desk Gloss White",
              "productKey": 2213219,
              "priceInfo": { "price": 339.0 },
              "imageGroups": [
                { "colourAttribute": "", "imageUrls": ["https://img.test/R2213219_00.jpg", "https://img.test/R2213219_60.jpg"] }
              ],
              "productImageUrl": "https://img.test/R2213219_00.jpg",
              "brandCode": "JASPER J",
              "brandDescription": "Jasper J",
              "inventory": { "available": true, "preorderable": false, "backorderable": false, "soh": 29 },
              "promotions": [
                {
                  "promotionId": "36048-1",
                  "dealDescription": "Buy 1 get 1 HALF PRICE Office Chairs & Desks s",
                  "isMarketClubExclusive": true,
                  "description": "Buy 1 get 1 HALF PRICE Office Chairs & Desks",
                  "tags": ["badge_bogo50off"]
                },
                {
                  "promotionId": "twl-omni-Promo",
                  "dealDescription": "Omni Products",
                  "isMarketClubExclusive": false,
                  "description": "Omni Products Promo",
                  "tags": ["ExcludeFromRefinement"]
                }
              ],
              "productId": "R2213219",
              "categoryId": "homegarden-furniture-officedeskschairs-officedesks",
              "productDescription": "Minimalist yet elegant.",
              "productBadges": [
                { "id": "mobile:oversize", "definition": { "position": "D", "order": 100 } }
              ],
              "isOversized": true,
              "soldOnline": "Y",
              "isClickAndCollect": true,
              "isMarketPlace": false
            },
            {
              "productName": "Caltex Havoline ATF J Transmission Fluid 1L",
              "productKey": 1588550,
              "priceInfo": { "price": 14.0 },
              "imageGroups": [{ "colourAttribute": "", "imageUrls": ["https://img.test/R1588550_40.jpg"] }],
              "productImageUrl": "https://img.test/R1588550_40.jpg",
              "brandCode": "CALTEX",
              "brandDescription": "Caltex",
              "inventory": { "available": false, "soh": 0 },
              "promotions": [
                {
                  "promotionId": "twl-omni-Promo",
                  "dealDescription": "Omni Products",
                  "isMarketClubExclusive": false,
                  "description": "Omni Products Promo",
                  "tags": ["ExcludeFromRefinement"]
                }
              ],
              "productId": "R1588550",
              "productBadges": [
                { "id": "clearance", "definition": { "position": "C", "description": "CLEARANCE", "order": 80 } }
              ],
              "soldOnline": "Y"
            }
          ],
          "searchTerm": "j",
          "suggestions": {},
          "total": 225,
          "facets": [{ "id": "brand", "name": "Brand", "totalCount": 33, "values": [] }],
          "sortOptions": [{ "id": "default-navigation-option", "name": "Best Match" }],
          "guest": true,
          "apiVersion": 4.9
        }
    """.trimIndent()

    @Test
    fun mapsTheRealSearchPayloadToDomain() {
        val result = mapper.toSearchResult(json.decodeFromString<SearchResponseDto>(searchSample), start = 0)

        assertEquals(225, result.total)
        assertEquals(2, result.products.size)

        val desk = result.products.first()
        assertEquals("R2213219", desk.id)
        assertEquals("Jasper J Loop Desk Gloss White", desk.name)
        assertEquals(339.0, desk.price, "price is nested under priceInfo")
        assertEquals("https://img.test/R2213219_00.jpg", desk.imageUrl)
        assertEquals("Minimalist yet elegant.", desk.description)
    }




    @Test
    fun fallsBackToTheFirstImageGroupWhenThereIsNoProductImageUrl() {
        val dto = json.decodeFromString<SearchResponseDto>(
            """{"products":[{"productId":"R1","imageGroups":[{"imageUrls":["","https://img.test/b.jpg"]}]}]}"""
        )

        assertEquals("https://img.test/b.jpg", mapper.toSearchResult(dto, start = 0).products.single().imageUrl)
    }

    @Test
    fun dropsSearchItemsWithoutAProductId() {
        val dto = json.decodeFromString<SearchResponseDto>(
            """{"products":[{"productName":"Ghost"},{"productId":" ","productName":"Blank"},{"productId":"R1"}]}"""
        )

        assertEquals(listOf("R1"), mapper.toSearchResult(dto, start = 0).products.map { it.id })
    }

    @Test
    fun unknownOrMissingSearchFieldsDegradeInsteadOfThrowing() {
        val result = mapper.toSearchResult(json.decodeFromString("""{"somethingElse":true}"""), start = 0)

        assertTrue(result.products.isEmpty())
        assertEquals(0, result.total, "an absent total falls back to the item count")
    }

    @Test
    fun hasMoreReflectsTheServerTotal() {
        assertTrue(mapper.toSearchResult(json.decodeFromString(searchSample), start = 0).hasMore, "2 of 225 shown")
        assertFalse(mapper.toSearchResult(json.decodeFromString(searchSample), start = 223).hasMore, "223 + 2 == 225")
    }
}
