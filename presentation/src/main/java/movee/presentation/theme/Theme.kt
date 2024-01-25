package movee.presentation.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun MoveeTheme(content: @Composable () -> Unit) {
    val colors = lightColors(
        primary = AppColors.DarkCyan,
        background = Color.White,
        surface = Color.White,
    )

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

class AppTypography(
    val h2Bold: TextStyle,
    val h3Bold: TextStyle,
    val h4Bold: TextStyle,
    val bodyLRegular: TextStyle,
    val bodyLBold: TextStyle,
    val bodyMRegular: TextStyle,
    val bodySRegular: TextStyle,
)

val MaterialTheme.appTypography: AppTypography by lazy {
    AppTypography(
        h2Bold = TextStyle(
            fontWeight = FontWeight.W700,
            fontSize = 28.sp,
            lineHeight = 34.sp,
        ),
        h3Bold = TextStyle(
            fontWeight = FontWeight.W700,
            fontSize = 22.sp,
            lineHeight = 28.sp
        ),
        h4Bold = TextStyle(
            fontWeight = FontWeight.W700,
            fontSize = 18.sp,
            lineHeight = 24.sp,
        ),
        bodyLRegular = TextStyle(
            fontWeight = FontWeight.W400,
            fontSize = 17.sp,
            lineHeight = 22.sp,
        ),
        bodyLBold = TextStyle(
            fontWeight = FontWeight.W700,
            fontSize = 17.sp,
            lineHeight = 22.sp,
        ),
        bodyMRegular = TextStyle(
            fontWeight = FontWeight.W400,
            fontSize = 16.sp,
            lineHeight = 20.sp,
        ),
        bodySRegular = TextStyle(
            fontWeight = FontWeight.W400,
            fontSize = 14.sp,
            lineHeight = 28.sp,
        )
    )
}