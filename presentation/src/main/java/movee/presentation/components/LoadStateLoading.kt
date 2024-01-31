package movee.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState

@Composable
fun CombinedLoadStates.LoadingWhenAppend(
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 6.dp,
    color: Color = Color.Gray
) {
    if (this.append == LoadState.Loading) {
        Indicator(
            modifier = modifier.fillMaxWidth(),
            strokeWidth = strokeWidth,
            color = color
        )
    }
}

@Composable
fun CombinedLoadStates.LoadingWhenRefresh(
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 6.dp,
    color: Color = Color.Gray
) {
    if (this.refresh == LoadState.Loading) {
        Indicator(
            modifier = modifier.fillMaxWidth(),
            strokeWidth = strokeWidth,
            color = color
        )
    }
}

@Composable
private fun Indicator(
    modifier: Modifier,
    size: Dp = 100.dp,
    strokeWidth: Dp,
    color: Color
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(size),
            strokeWidth = strokeWidth,
            color = color
        )
    }
}