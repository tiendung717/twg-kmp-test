package nz.co.warehouseandroidtest.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun dataStorePlatformModule(): Module = module {
    single<DataStore<Preferences>> {
        createDataStore { androidContext().filesDir.resolve(DATA_STORE_FILE_NAME).absolutePath }
    }
}
