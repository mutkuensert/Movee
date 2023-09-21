package movee.data.util

import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

fun Double.withDecimalDigits(number: Int = 1): Double {
    var diesisForDecimals = ""

    repeat(number) {
        diesisForDecimals += "#"
    }

    val symbols = DecimalFormatSymbols().apply { decimalSeparator = '.' }
    val df = DecimalFormat("#.$diesisForDecimals", symbols)
    df.roundingMode = RoundingMode.HALF_EVEN
    return df.format(this).toDouble()
}