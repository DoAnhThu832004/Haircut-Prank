package com.example.comthupohaircut.presentation.screens.setting.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.comthupohaircut.ui.theme.ColorOnGradientSecondary
import com.example.comthupohaircut.ui.theme.ColorWave
import com.example.comthupohaircut.ui.theme.ScreenGradientEnd
import com.example.comthupohaircut.ui.theme.ScreenGradientStart
import java.nio.file.WatchEvent

@Composable
fun SettingMenuItem(
    title: String,
    iconRes: Int,
    onClick: () -> Unit
) {
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            ColorOnGradientSecondary,
            ColorWave
        )
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(gradientBrush)
            .clickable{ onClick() }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = Color.White
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = Color.White
        )
    }
}
