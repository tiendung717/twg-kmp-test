package nz.co.warehouseandroidtest.shared.common

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Network.nw_path_get_status
import platform.Network.nw_path_monitor_create
import platform.Network.nw_path_monitor_set_queue
import platform.Network.nw_path_monitor_set_update_handler
import platform.Network.nw_path_monitor_start
import platform.Network.nw_path_status_satisfied
import platform.Network.nw_path_t
import platform.darwin.dispatch_get_main_queue
import nz.co.warehouseandroidtest.shared.common.NetworkMonitor

@OptIn(ExperimentalForeignApi::class)
class IosNetworkMonitor : NetworkMonitor {

    private val _isOnline = MutableStateFlow(true)
    override val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()

    private var lastPath: nw_path_t = null

    private val monitor = nw_path_monitor_create()

    init {
        nw_path_monitor_set_update_handler(monitor) { path ->
            lastPath = path
            _isOnline.value = path.isSatisfied()
        }
        nw_path_monitor_set_queue(monitor, dispatch_get_main_queue())
        nw_path_monitor_start(monitor)
    }

    override fun refresh() {
        _isOnline.value = lastPath?.isSatisfied() ?: true
    }

    private fun nw_path_t.isSatisfied(): Boolean =
        nw_path_get_status(this) == nw_path_status_satisfied
}

actual fun networkMonitorModule(): Module = module {
    single<NetworkMonitor> { IosNetworkMonitor() }
}
