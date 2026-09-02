package nz.co.warehouseandroidtest

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import nz.co.warehouseandroidtest.shared.di.KoinStarter

class AndroidApp : Application() {

    override fun onCreate() {
        super.onCreate()

        KoinStarter.initializeDependencyGraph {
            androidContext(this@AndroidApp)
            androidLogger()
        }
    }
}