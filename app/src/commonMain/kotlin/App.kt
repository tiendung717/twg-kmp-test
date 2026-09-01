import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import nz.co.warehouseandroidtest.designsystem.theme.AppTheme
import nz.co.warehouseandroidtest.feature.product.presentation.ScreenProduct
import nz.co.warehouseandroidtest.feature.search.presentation.ScreenSearch
import nz.co.warehouseandroidtest.navigation.NavScreen
import nz.co.warehouseandroidtest.navigation.Navigation
import nz.co.warehouseandroidtest.shared.common.NetworkMonitor
import nz.co.warehouseandroidtest.shared.presentation.NoInternetDialog
import org.koin.compose.koinInject

@Composable
fun App() {
    AppTheme {
        val networkMonitor: NetworkMonitor = koinInject()
        val isOnline by networkMonitor.isOnline.collectAsState()

        val navController = rememberNavController()
        Navigation(
            navController = navController,
            startingDestination = NavScreen.Search
        ) {
            composable<NavScreen.Search> {
                ScreenSearch(
                    onProductClick = {
                        navController.navigate(NavScreen.Product(it))
                    }
                )
            }
            composable<NavScreen.Product> {
                ScreenProduct(onBack = { navController.popBackStack() })
            }
        }

        if (!isOnline) {
            NoInternetDialog(onRetry = networkMonitor::refresh)
        }
    }
}
