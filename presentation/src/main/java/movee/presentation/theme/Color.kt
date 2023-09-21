package movee.presentation.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils

object AppColors {
    val DarkCyan = Color(0xFF55868C)
    val FavoriteStar = Color(0xFFFFC107)
}

fun Color.isDark(): Boolean {
    return ColorUtils.calculateLuminance(this.toArgb()) < 0.1
}