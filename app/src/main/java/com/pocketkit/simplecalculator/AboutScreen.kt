package com.pocketkit.simplecalculator

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pocketkit.simplecalculator.ui.theme.*

@Composable
fun AboutScreen(
    modifier: Modifier = Modifier,
    currentTheme: AppTheme = AppTheme.System
) {
    val isSystemDark = isSystemInDarkTheme()
    val isDark = when (currentTheme) {
        AppTheme.Light -> false
        AppTheme.Dark -> true
        AppTheme.System -> isSystemDark
    }
    val gradientColors = if (isDark) {
        listOf(DarkBackgroundTop, DarkBackgroundMiddle, DarkBackgroundBottom)
    } else {
        listOf(LightBackgroundTop, LightBackgroundMiddle, LightBackgroundBottom)
    }
    val textColor = MaterialTheme.colorScheme.onBackground

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(colors = gradientColors)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Manually compose the icon to avoid potential crashes with adaptive icon XML loading
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(Color(0xFF0B0F19), androidx.compose.foundation.shape.RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_app_icon),
                    contentDescription = "App Icon",
                    modifier = Modifier.fillMaxSize().padding(16.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "About App",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Open Calculator v1.0",
                fontSize = 16.sp,
                color = textColor.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "A simple yet powerful calculator for your daily needs. Supports basic arithmetic and scientific operations.",
                fontSize = 14.sp,
                color = textColor.copy(alpha = 0.6f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
        }
    }
}
