package movee.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest

@Composable
fun Poster(
    modifier: Modifier = Modifier,
    posterUrl: String?,
    posterHeight: Dp = 150.dp,
    posterElevation: Dp = 3.dp
) {
    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(posterUrl)
            .crossfade(true)
            .build(),
        loading = {
            Box(
                modifier = Modifier
                    .height(posterHeight)
                    .width(100.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.Gray)
            }
        },
        modifier = modifier
            .height(posterHeight)
            .shadow(elevation = posterElevation, shape = MaterialTheme.shapes.medium)
            .clip(MaterialTheme.shapes.medium),
        contentDescription = "Poster"
    )
}