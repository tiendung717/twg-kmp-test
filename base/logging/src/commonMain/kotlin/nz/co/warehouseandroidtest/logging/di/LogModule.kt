package nz.co.warehouseandroidtest.logging.di

import nz.co.warehouseandroidtest.logging.AppLogger
import nz.co.warehouseandroidtest.logging.AppLoggerConfig
import org.koin.core.annotation.Module
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single

@Module
class LogModule {
    @Provided
    @Single
    fun provideAppLoggerConfig(): AppLoggerConfig {
        return AppLoggerConfig()
    }

    @Provided
    @Single
    fun provideAppLogger(): AppLogger {
        return AppLogger()
    }
}