package nz.co.warehouseandroidtest.feature.product.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import nz.co.warehouseandroidtest.designsystem.components.ErrorView
import nz.co.warehouseandroidtest.designsystem.components.LoadingView
import nz.co.warehouseandroidtest.designsystem.theme.AppTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import nz.co.warehouseandroidtest.feature.product.domain.model.ProductDetail
import nz.co.warehouseandroidtest.feature.product.presentation.components.CategoryChip
import nz.co.warehouseandroidtest.feature.product.presentation.components.ProductHeader
import nz.co.warehouseandroidtest.feature.product.presentation.components.ProductSection
import nz.co.warehouseandroidtest.feature.product.resources.Res
import nz.co.warehouseandroidtest.feature.product.resources.error_generic
import nz.co.warehouseandroidtest.feature.product.resources.product_back
import nz.co.warehouseandroidtest.feature.product.resources.product_availability_title
import nz.co.warehouseandroidtest.feature.product.resources.product_in_stock
import nz.co.warehouseandroidtest.feature.product.resources.product_in_stock_count
import nz.co.warehouseandroidtest.feature.product.resources.product_out_of_stock
import nz.co.warehouseandroidtest.feature.product.resources.product_promotions_title
import nz.co.warehouseandroidtest.feature.product.resources.product_categories_title
import nz.co.warehouseandroidtest.feature.product.resources.product_description_title
import nz.co.warehouseandroidtest.feature.product.resources.product_features_title
import nz.co.warehouseandroidtest.feature.product.resources.product_item_number

@Composable
fun ScreenProduct(
    onBack: () -> Unit,
    viewModel: ProductViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()

    ProductContent(
        state = state,
        onBack = onBack,
        onRetry = viewModel::retry,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductContent(
    state: ProductUiState,
    onBack: () -> Unit,
    onRetry: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(Res.string.product_back),
                        )
                    }
                },
            )
        },
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when {
                state.isLoading -> LoadingView(modifier = Modifier.align(Alignment.Center))

                state.error != null -> ErrorView(
                    message = stringResource(Res.string.error_generic),
                    onRetry = onRetry,
                    modifier = Modifier.align(Alignment.Center),
                )

                state.product != null -> ProductDetailBody(requireNotNull(state.product))
            }
        }
    }
}

@Composable
private fun ProductDetailBody(product: ProductDetail) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(16.dp),
    ) {
        item(key = "header") {
            ProductHeader(
                imageUrls = product.imageUrls,
                productName = product.name,
                brand = product.brand,
                price = product.price,
            )
            SectionDivider()
        }

        item(key = "availability") {
            ProductSection(title = stringResource(Res.string.product_availability_title)) {
                Text(
                    text = when {
                        !product.isAvailable -> stringResource(Res.string.product_out_of_stock)
                        product.stockOnHand != null ->
                            stringResource(Res.string.product_in_stock_count, product.stockOnHand)
                        else -> stringResource(Res.string.product_in_stock)
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = when {
                        product.isAvailable -> MaterialTheme.colorScheme.primary
                        else -> MaterialTheme.colorScheme.error
                    },
                )
            }
            SectionDivider()
        }

        if (product.promotions.isNotEmpty()) {
            item(key = "promotions") {
                ProductSection(title = stringResource(Res.string.product_promotions_title)) {
                    product.promotions.forEach { promotion ->
                        Text(
                            text = "\u2022 $promotion",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 12.dp),
                        )
                    }
                }
                SectionDivider()
            }
        }

        product.description?.let { description ->
            item(key = "description") {
                ProductSection(title = stringResource(Res.string.product_description_title)) {
                    Text(text = description, style = MaterialTheme.typography.bodyLarge)
                }
                SectionDivider()
            }
        }

        if (product.features.isNotEmpty()) {
            item(key = "features") {
                ProductSection(title = stringResource(Res.string.product_features_title)) {
                    product.features.forEach { feature ->
                        Text(
                            text = "\u2022 $feature",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 12.dp),
                        )
                    }
                }
                SectionDivider()
            }
        }

        product.itemNumber?.let { itemNumber ->
            item(key = "itemNumber") {
                ProductSection(title = stringResource(Res.string.product_item_number)) {
                    Text(text = itemNumber, style = MaterialTheme.typography.bodyLarge)
                }
                SectionDivider()
            }
        }

        if (product.categories.isNotEmpty()) {
            item(key = "categories") {
                ProductSection(title = stringResource(Res.string.product_categories_title)) {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(product.categories) { CategoryChip(label = it) }
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionDivider() {
    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
}

@Preview
@Composable
private fun ScreenProductLoadingPreview() {
    AppTheme {
        ProductContent(state = ProductUiState(isLoading = true), onBack = {}, onRetry = {})
    }
}

@Preview
@Composable
private fun ScreenProductErrorPreview() {
    AppTheme {
        ProductContent(
            state = ProductUiState(error = IllegalStateException("No connection")),
            onBack = {},
            onRetry = {},
        )
    }
}

@Preview
@Composable
private fun ScreenProductSoldOutPreview() {
    AppTheme {
        ProductContent(
            state = ProductUiState(
                product = SamplePreviewProduct.copy(
                    isAvailable = false,
                    stockOnHand = 0,
                ),
            ),
            onBack = {},
            onRetry = {},
        )
    }
}

private val SamplePreviewProduct = ProductDetail(
    id = "R2767564",
    name = "Brother Inkvestment Tank MFC-J4340DWXL Printer",
    brand = "Brother",
    description = "The stylish MFC-J4340DW XL is a business-quality multifunction device offering print, copy, scan and fax functionality.",
    imageUrls = listOf(
        "https://beta.test/R2767564_30.jpg",
        "https://beta.test/R2767564_31.jpg",
        "https://beta.test/R2767564_32.jpg",
    ),
    price = 449.95,
    features = listOf(
        "Print up to A4",
        "Wireless connectivity",
        "AirPrint / Mopria",
        "20-sheet automatic document feeder (ADF)",
        "Inkvestment Tank technology - with 2 year of ink in the box",
    ),
    itemNumber = "2767564",
    categories = listOf("Inkjet Printers", "Printers & Scanners", "Computers & Tablets", "Electronics & Gaming"),
    stockOnHand = 16,
)
