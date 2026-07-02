package com.example.frontend.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LoadingView(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    if (isLoading) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color(0x7F000000)), // semi-transparent background
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(50.dp),
                color = Color(0xFF6C63FF) // PrimaryGradientStart
            )
        }
    }
}
