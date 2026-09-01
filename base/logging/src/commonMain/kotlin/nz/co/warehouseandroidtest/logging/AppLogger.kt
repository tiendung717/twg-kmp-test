package nz.co.warehouseandroidtest.logging

import co.touchlab.kermit.ExperimentalKermitApi
import co.touchlab.kermit.Logger
import co.touchlab.kermit.crashlytics.CrashlyticsLogWriter

class AppLogger {
    companion object {
        internal var isDebug = false

        fun d(messageString: String, throwable: Throwable? = null, tag: String = "") {
            Logger.d(tag = tag, throwable = throwable, messageString = messageString)
        }

        fun d(throwable: Throwable? = null, tag: String = "", message: () -> String) {
            d(throwable = throwable, tag = tag, messageString = message())
        }

        fun e(messageString: String, throwable: Throwable? = null, tag: String = "") {
            Logger.e(tag = tag, throwable = throwable, messageString = messageString)
        }

        fun e(throwable: Throwable? = null, tag: String = "", message: () -> String) {
            e(tag = tag, throwable = throwable, messageString = message())
        }
    }
}

class AppLoggerConfig {
    @OptIn(ExperimentalKermitApi::class)
    fun config(isDebug: Boolean) {
        AppLogger.isDebug = isDebug
        if (!isDebug) {
            Logger.setLogWriters(CrashlyticsLogWriter())
        }
    }
}