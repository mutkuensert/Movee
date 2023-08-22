package com.mutkuensert.movee.core

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
fun CombinedLoadStates.LoadingIfAppend(
    modifier: Modifier = Modifier,
    padding: Dp = 10.dp,
    indicatorSize: Dp = 20.dp,
    strokeWidth: Dp = 6.dp,
    color: Color = Color.Gray
) {
    if (this.append == LoadState.Loading) {
        Indicator(
            modifier = if (modifier == Modifier) Modifier
                .padding(padding)
                .fillMaxWidth()
            else Modifier,
            indicatorSize = indicatorSize,
            strokeWidth = strokeWidth,
            color = color
        )
    }
}

@Composable
fun CombinedLoadStates.LoadingIfRefresh(
    modifier: Modifier = Modifier,
    padding: Dp = 50.dp,
    indicatorSize: Dp = 100.dp,
    strokeWidth: Dp = 6.dp,
    color: Color = Color.Gray
) {
    if (this.refresh == LoadState.Loading) {
        Indicator(
            modifier = if (modifier == Modifier) Modifier
                .padding(padding)
                .fillMaxWidth()
            else Modifier,
            indicatorSize = indicatorSize,
            strokeWidth = strokeWidth,
            color = color
        )
    }
}

@Composable
private fun Indicator(
    modifier: Modifier,
    indicatorSize: Dp,
    strokeWidth: Dp,
    color: Color
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(indicatorSize),
            strokeWidth = strokeWidth,
            color = color
        )
    }
}