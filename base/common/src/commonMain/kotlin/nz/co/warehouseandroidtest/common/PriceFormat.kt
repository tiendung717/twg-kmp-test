package nz.co.warehouseandroidtest.common

import kotlin.math.abs
import kotlin.math.floor

fun formatPrice(value: Double): String {
    val cents = floor(abs(value) * 100 + 0.5).toLong()
    val sign = if (value < 0) "-" else ""
    return "$sign$${cents / 100}.${(cents % 100).toString().padStart(2, '0')}"
}
