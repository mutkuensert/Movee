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
    val feedContentTitle: TextStyle,
    val feedVoteAverage: TextStyle,
    val feedShowTitle: TextStyle,
    val showDetailShowTitle: TextStyle,
    val showDetailVoteAverage: TextStyle,
    val showDetailRuntime: TextStyle,
    val showDetailCastName: TextStyle,
    val showDetailCharacterName: TextStyle,
    val showDetailSeasonInfo: TextStyle,
    val showDetailEpisodeInfo: TextStyle,
)

val MaterialTheme.appTypography: AppTypography by lazy {
    AppTypography(
        feedContentTitle = TextStyle(
            color = Color.LightGray,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 20.sp
        ),
        feedVoteAverage = TextStyle(
            color = Color.Gray,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        ),
        feedShowTitle = TextStyle(
            color = Color.DarkGray,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 20.sp
        ),
        showDetailShowTitle = TextStyle(
            color = Color.DarkGray,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 30.sp
        ),
        showDetailVoteAverage = TextStyle(
            color = Color.Gray,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        ),
        showDetailRuntime = TextStyle(
            color = Color.DarkGray,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        ),
        showDetailCastName = TextStyle(
            color = Color.DarkGray,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 20.sp
        ),
        showDetailCharacterName = TextStyle(
            color = Color.Gray,
            fontWeight = FontWeight.Bold
        ),
        showDetailSeasonInfo = TextStyle(
            color = Color.DarkGray,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        ),
        showDetailEpisodeInfo = TextStyle(
            color = Color.DarkGray,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
    )
}