package nz.co.warehouseandroidtest.shared.common

import kotlinx.coroutines.flow.StateFlow
import org.koin.core.module.Module

interface NetworkMonitor {
    val isOnline: StateFlow<Boolean>
    fun refresh()
}

expect fun networkMonitorModule(): Module
