package movee.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun NestedVerticalScroll(
    modifier: Modifier = Modifier,
    topContent: @Composable () -> Unit,
    bottomContent: @Composable (lazyListContentPadding: PaddingValues) -> Unit
) {
    val localDensity = LocalDensity.current

    var topContentHeight by remember { mutableStateOf(0.dp) }
    var topContentHeightPx by remember { mutableFloatStateOf(0f) }
    var topContentOffsetHeightPx by remember { mutableFloatStateOf(0f) }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffset = topContentOffsetHeightPx + delta
                topContentOffsetHeightPx = newOffset.coerceIn(-topContentHeightPx, 0f)
                return Offset.Zero
            }
        }
    }

    Box(modifier = modifier.nestedScroll(nestedScrollConnection)) {
        bottomContent.invoke(PaddingValues(top = topContentHeight))

        Box(modifier = Modifier
            .onSizeChanged {
                topContentHeightPx = it.height.toFloat()
                topContentHeight = with(localDensity) { it.height.toDp() }
            }
            .offset {
                IntOffset(
                    x = 0,
                    y = topContentOffsetHeightPx.roundToInt()
                )
            }) { topContent.invoke() }
    }
}