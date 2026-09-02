package nz.co.warehouseandroidtest.shared.di

import nz.co.warehouseandroidtest.data.di.DataModule
import nz.co.warehouseandroidtest.data.local.dataStorePlatformModule
import nz.co.warehouseandroidtest.feature.product.di.ProductModule
import nz.co.warehouseandroidtest.feature.search.di.SearchModule
import nz.co.warehouseandroidtest.logging.di.LogModule
import nz.co.warehouseandroidtest.shared.common.networkMonitorModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.ksp.generated.module

object KoinStarter {
    fun initializeDependencyGraph(onKoinStart: KoinAppDeclaration = {}) {
        startKoin {
            onKoinStart()
            modules(
                DataModule().module,
                SearchModule().module,
                ProductModule().module,
            )
            modules(
                LogModule().module,
                dataStorePlatformModule(),
                networkMonitorModule(),
            )
        }
    }
}
