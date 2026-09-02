package nz.co.warehouseandroidtest.feature.search.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.remember
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import nz.co.warehouseandroidtest.designsystem.components.EmptyView
import nz.co.warehouseandroidtest.designsystem.components.ErrorView
import nz.co.warehouseandroidtest.designsystem.components.LoadingView
import nz.co.warehouseandroidtest.feature.search.presentation.components.ProductCell
import nz.co.warehouseandroidtest.designsystem.components.SearchBox
import nz.co.warehouseandroidtest.designsystem.theme.AppTheme
import kotlinx.coroutines.flow.flowOf
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import nz.co.warehouseandroidtest.common.formatPrice
import nz.co.warehouseandroidtest.feature.search.domain.model.Product
import nz.co.warehouseandroidtest.feature.search.resources.Res
import nz.co.warehouseandroidtest.feature.search.resources.error_generic
import nz.co.warehouseandroidtest.feature.search.resources.search_empty_message
import nz.co.warehouseandroidtest.feature.search.resources.search_empty_title
import nz.co.warehouseandroidtest.feature.search.resources.search_placeholder

@Composable
fun ScreenSearch(
    onProductClick: (String) -> Unit,
    viewModel: SearchViewModel = koinViewModel(),
) {
    val query by viewModel.query.collectAsState()
    val products = viewModel.products.collectAsLazyPagingItems()

    // The field owns text *and* selection: the ViewModel survives the trip to the product screen but
    // this composable does not, and a plain String would rebuild the cursor at offset 0 on the way back.
    var field by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(query, TextRange(query.length)))
    }

    SearchContent(
        query = field,
        products = products,
        onQueryChange = {
            field = it
            viewModel.onQueryChange(it.text)
        },
        onProductClick = onProductClick,
    )
}

@Composable
private fun SearchContent(
    query: TextFieldValue,
    products: LazyPagingItems<Product>,
    onQueryChange: (TextFieldValue) -> Unit,
    onProductClick: (String) -> Unit,
) {
    val gridState = rememberLazyGridState()
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            Surface(
                modifier = Modifier.windowInsetsPadding(
                    WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top)
                )
            ) {
                SearchBox(
                    value = query,
                    onValueChange = onQueryChange,
                    placeholder = stringResource(Res.string.search_placeholder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                )
            }
        },
    ) { padding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            val columns = if (maxWidth > maxHeight) LANDSCAPE_GRID_COLUMNS else PORTRAIT_GRID_COLUMNS

            LazyVerticalGrid(
                columns = GridCells.Fixed(columns),
                state = gridState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 1.dp, top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(1.dp),
                verticalArrangement = Arrangement.spacedBy(1.dp),
            ) {
                items(
                    count = products.itemCount,
                    key = products.itemKey { it.id },
                ) { index ->
                    products[index]?.let { product ->
                        ProductCell(
                            title = product.name,
                            imageUrl = product.imageUrl,
                            description = product.description,
                            price = product.price?.let(::formatPrice),
                            onClick = { onProductClick(product.id) },
                        )
                    }
                }

                if (query.text.isNotBlank()) {
                    searchLoadState(
                        query = query.text,
                        loadState = products.loadState,
                        itemCount = products.itemCount,
                        onRetry = products::retry
                    )
                }
            }
        }
    }
}

private fun LazyGridScope.searchLoadState(
    query: String,
    loadState: CombinedLoadStates,
    itemCount: Int,
    onRetry: () -> Unit,
) {
    val refresh = loadState.refresh
    val append = loadState.append

    when {
        refresh is LoadState.Loading -> fullSpanItem { LoadingView() }

        refresh is LoadState.Error -> fullSpanItem {
            ErrorView(
                message = stringResource(Res.string.error_generic),
                onRetry = onRetry,
            )
        }

        itemCount == 0 -> fullSpanItem {
            EmptyView(
                title = stringResource(Res.string.search_empty_title),
                message = stringResource(Res.string.search_empty_message, query),
            )
        }
    }

    if (append is LoadState.Loading) {
        fullSpanItem { LoadingView() }
    }

    if (append is LoadState.Error) {
        fullSpanItem {
            ErrorView(
                message = stringResource(Res.string.error_generic),
                onRetry = onRetry,
                showIcon = false,
            )
        }
    }
}

private fun LazyGridScope.fullSpanItem(content: @Composable () -> Unit) {
    item(span = { GridItemSpan(maxLineSpan) }) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface),
            contentAlignment = Alignment.Center,
        ) { content() }
    }
}

@Composable
private fun previewProducts(
    products: List<Product> = SamplePreviewProducts,
    loadStates: LoadStates = LoadStates(
        refresh = LoadState.NotLoading(endOfPaginationReached = true),
        prepend = LoadState.NotLoading(endOfPaginationReached = true),
        append = LoadState.NotLoading(endOfPaginationReached = true),
    ),
): LazyPagingItems<Product> =
    flowOf(PagingData.from(products, loadStates)).collectAsLazyPagingItems()

@Preview
@Composable
private fun ScreenSearchResultsPreview() {
    AppTheme {
        SearchContent(
            query = TextFieldValue("jacket"),
            products = previewProducts(),
            onQueryChange = {},
            onProductClick = {},
        )
    }
}

@Preview
@Composable
private fun ScreenSearchLoadingPreview() {
    AppTheme {
        SearchContent(
            query = TextFieldValue("jacket"),
            products = previewProducts(
                products = emptyList(),
                loadStates = LoadStates(
                    refresh = LoadState.Loading,
                    prepend = LoadState.NotLoading(endOfPaginationReached = true),
                    append = LoadState.NotLoading(endOfPaginationReached = true),
                ),
            ),
            onQueryChange = {},
            onProductClick = {},
        )
    }
}

@Preview
@Composable
private fun ScreenSearchEmptyPreview() {
    AppTheme {
        SearchContent(
            query = TextFieldValue("unicorn onesie"),
            products = previewProducts(products = emptyList()),
            onQueryChange = {},
            onProductClick = {},
        )
    }
}

@Preview
@Composable
private fun ScreenSearchErrorPreview() {
    AppTheme {
        SearchContent(
            query = TextFieldValue("jacket"),
            products = previewProducts(
                products = emptyList(),
                loadStates = LoadStates(
                    refresh = LoadState.Error(IllegalStateException("No connection")),
                    prepend = LoadState.NotLoading(endOfPaginationReached = true),
                    append = LoadState.NotLoading(endOfPaginationReached = true),
                ),
            ),
            onQueryChange = {},
            onProductClick = {},
        )
    }
}

@Preview
@Composable
private fun ScreenSearchAppendingPreview() {
    AppTheme {
        SearchContent(
            query = TextFieldValue("jacket"),
            products = previewProducts(
                loadStates = LoadStates(
                    refresh = LoadState.NotLoading(endOfPaginationReached = false),
                    prepend = LoadState.NotLoading(endOfPaginationReached = true),
                    append = LoadState.Loading,
                ),
            ),
            onQueryChange = {},
            onProductClick = {},
        )
    }
}

@Preview
@Composable
private fun ScreenSearchDarkPreview() {
    AppTheme(useDarkTheme = true) {
        SearchContent(
            query = TextFieldValue("jacket"),
            products = previewProducts(),
            onQueryChange = {},
            onProductClick = {},
        )
    }
}

@Preview
@Composable
private fun ScreenSearchIdlePreview() {
    AppTheme {
        SearchContent(
            query = TextFieldValue(""),
            products = previewProducts(
                products = emptyList(),
                loadStates = LoadStates(
                    refresh = LoadState.Loading,
                    prepend = LoadState.NotLoading(endOfPaginationReached = true),
                    append = LoadState.NotLoading(endOfPaginationReached = true),
                ),
            ),
            onQueryChange = {},
            onProductClick = {},
        )
    }
}

@Preview
@Composable
private fun ScreenSearchLandscapePreview() {
    AppTheme {
        Box(modifier = Modifier.size(width = 800.dp, height = 400.dp)) {
            SearchContent(
                query = TextFieldValue("jacket"),
                products = previewProducts(),
                onQueryChange = {},
                onProductClick = {},
            )
        }
    }
}

private const val PORTRAIT_GRID_COLUMNS = 2
private const val LANDSCAPE_GRID_COLUMNS = 3

private val SamplePreviewProducts = listOf(
    Product(
        id = "R2213219",
        name = "Jasper J Loop Desk Gloss White",
        description = "Minimalist yet elegant, an ideal choice for home offices or small business set-ups.",
        imageUrl = null,
        price = 339.0,
    ),
    Product(
        id = "R2790106",
        name = "Brother MFC-J1010DW Inkjet Printer",
        description = "Sleek, simple and affordable all-in-one inkjet printer.",
        imageUrl = null,
        price = 149.95,
    ),
    Product(
        id = "R1588550",
        name = "Caltex Havoline ATF J Transmission Fluid 1L",
        description = null,
        imageUrl = null,
        price = 14.0,
    ),
    Product(
        id = "R2744266",
        name = "ACOTAR #4 A Court of Frost and Starlight by Sarah J Maas",
        description = "In this companion tale to the bestselling A Court of Thorns and Roses series.",
        imageUrl = null,
        price = 20.0,
    ),
)
