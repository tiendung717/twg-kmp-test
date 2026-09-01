package nz.co.warehouseandroidtest.common

import kotlin.test.Test
import kotlin.test.assertEquals

class PriceFormatTest {

    @Test
    fun formatsWholeDollars() {
        assertEquals("$45.00", formatPrice(45.0))
        assertEquals("$0.00", formatPrice(0.0))
    }

    @Test
    fun formatsCentsWithTwoDigits() {
        assertEquals("$0.05", formatPrice(0.05))
        assertEquals("$2.49", formatPrice(2.49))
        assertEquals("$1234.56", formatPrice(1234.56))
    }

    @Test
    fun roundsRatherThanTruncatingBinaryFloatError() {
        assertEquals("$19.99", formatPrice(19.99))
        assertEquals("$9.99", formatPrice(9.99))
        assertEquals("$89.99", formatPrice(89.99))
        assertEquals("$59.99", formatPrice(59.99))
    }

    @Test
    fun roundsHalfCentsAwayFromZero() {
        assertEquals("$1.13", formatPrice(1.125), "kotlin.math.round would give 1.12 (half to even)")
        assertEquals("$1.00", formatPrice(1.004))
    }

    @Test
    fun aDecimalHalfThatIsNotExactlyRepresentableRoundsDown() {
        assertEquals("$1.00", formatPrice(1.005), "1.005 * 100 is 100.49999999999999 in binary")
    }

    @Test
    fun keepsTheSignForNegativeValues() {
        assertEquals("-$5.00", formatPrice(-5.0))
    }
}
