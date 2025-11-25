package com.pocketkit.simplecalculator

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CalculatorButton(
    symbol: String? = null,
    icon: Int? = null,
    contentDescription: String? = null,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    textStyle: androidx.compose.ui.text.TextStyle = androidx.compose.ui.text.TextStyle(),
    isDark: Boolean = isSystemInDarkTheme(),
    shape: androidx.compose.ui.graphics.Shape = CircleShape,
    onClick: () -> Unit
) {

    // Advanced Glass Gradients
    val borderBrush = Brush.linearGradient(
        colors = listOf(
            Color.White.copy(alpha = if (isDark) 0.6f else 0.9f), // Top-Left Highlight (Reflection)
            Color.White.copy(alpha = 0.1f), // Middle
            Color.Transparent  // Bottom-Right
        ),
        start = Offset.Zero,
        end = Offset.Infinite
    )

    val backgroundBrush = if (isDark) {
        Brush.linearGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.1f),
                Color.White.copy(alpha = 0.03f)
            )
        )
    } else {
        Brush.linearGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.7f),
                Color.White.copy(alpha = 0.3f)
            )
        )
    }
    
    val shadowColor = if (isDark) Color.Black.copy(alpha = 0.3f) else Color.Black.copy(alpha = 0.1f)

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .shadow(
                elevation = 6.dp,
                shape = shape,
                ambientColor = shadowColor,
                spotColor = shadowColor
            )
            .clip(shape)
            .background(backgroundBrush)
            .border(1.5.dp, borderBrush, shape) // Thicker border for reflection
            .clickable { onClick() }
    ) {
        if (symbol != null) {
            Text(
                text = symbol,
                style = textStyle,
                fontSize = 36.sp,
                color = color
            )
        } else if (icon != null) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = contentDescription,
                tint = color
            )
        }
    }
}
