package com.mutkuensert.movee.core

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mutkuensert.androidphase.Phase

@Composable
fun <T : Any> Phase<T>.Loading(
    modifier: Modifier = Modifier,
    padding: Dp = 50.dp,
    indicatorSize: Dp = 100.dp,
    strokeWidth: Dp = 6.dp,
    color: Color = Color.Gray
) {
    val visible = this is Phase.Loading

    if (visible) {
        Column(
            modifier = if (modifier == Modifier) Modifier
                .padding(padding)
                .fillMaxSize() else modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(indicatorSize),
                strokeWidth = strokeWidth,
                color = color
            )
        }
    }
}