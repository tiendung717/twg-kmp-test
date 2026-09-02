package nz.co.warehouseandroidtest.shared.common

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import nz.co.warehouseandroidtest.logging.AppLogger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

class AndroidNetworkMonitor(context: Context) : NetworkMonitor {

    private val connectivityManager =
        context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val _isOnline = MutableStateFlow(currentlyOnline())
    override val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()

    private val callback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) = update()
        override fun onLost(network: Network) = update()
        override fun onUnavailable() = update()
        override fun onCapabilitiesChanged(network: Network, capabilities: NetworkCapabilities) =
            update()
    }

    init {
        runCatching { connectivityManager.registerDefaultNetworkCallback(callback) }
            .onFailure { AppLogger.e(messageString = "registerDefaultNetworkCallback failed", throwable = it) }
    }

    override fun refresh() = update()

    private fun update() {
        _isOnline.value = currentlyOnline()
    }

    private fun currentlyOnline(): Boolean =
        connectivityManager.allNetworks.any { network ->
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return@any false
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) &&
                !capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
        }
}

actual fun networkMonitorModule(): Module = module {
    single<NetworkMonitor> { AndroidNetworkMonitor(androidContext()) }
}
