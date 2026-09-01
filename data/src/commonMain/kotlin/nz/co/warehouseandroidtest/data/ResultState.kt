package nz.co.warehouseandroidtest.data

import kotlinx.coroutines.CancellationException
import nz.co.warehouseandroidtest.logging.AppLogger

suspend fun <T> safeApiCall(block: suspend () -> T): ResultState<T> {
    try {
        val value = block()
        return ResultState.Success(value)
    } catch (cancellation: CancellationException) {
        throw cancellation
    } catch (throwable: Throwable) {
        AppLogger.e(messageString = "Exception occurred", throwable = throwable)
        return ResultState.Failure(throwable)
    }
}

sealed class ResultState<out T> {
    data object Loading : ResultState<Nothing>()
    data class Success<out T>(val data: T) : ResultState<T>()
    data class Failure(val throwable: Throwable) : ResultState<Nothing>()
}