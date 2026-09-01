package nz.co.warehouseandroidtest.data

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertSame

class SafeApiCallTest {

    @Test
    fun wrapsAReturnedValueInSuccess() = runTest {
        val result = safeApiCall { 42 }

        assertEquals(42, assertIs<ResultState.Success<Int>>(result).data)
    }

    @Test
    fun wrapsAThrownExceptionInFailureWithoutRethrowing() = runTest {
        val boom = IllegalStateException("boom")

        val result = safeApiCall<Int> { throw boom }

        assertSame(boom, assertIs<ResultState.Failure>(result).throwable)
    }

    @Test
    fun rethrowsCancellationInsteadOfReportingItAsFailure() = runTest {
        assertFailsWith<CancellationException> {
            safeApiCall<Int> { throw CancellationException("cancelled") }
        }
    }
}
