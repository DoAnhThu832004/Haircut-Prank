package com.example.comthupohaircut.presentation.screens.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.comthupohaircut.R
import com.example.comthupohaircut.ui.theme.TitanOneFont

@Composable
fun SoundControlsSection(
    isLooping: Boolean,
    selectedTimerSeconds: Int,
    onToggleLoop: (Boolean) -> Unit,
    onOpenTimerDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Loop",
                fontFamily = TitanOneFont,
                fontSize = 16.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.width(10.dp))
            Switch(
                checked = isLooping,
                onCheckedChange = onToggleLoop,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color(0xFF8A38F5),
                    uncheckedThumbColor = Color.White.copy(alpha = 0.7f),
                    uncheckedTrackColor = Color.White.copy(alpha = 0.2f)
                )
            )
        }

        // 2. Điều khiển Hẹn giờ (Play after)
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Play after",
                fontFamily = TitanOneFont,
                fontSize = 15.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.width(10.dp))

            val timerLabel = when (selectedTimerSeconds) {
                5 -> "5s"
                10 -> "10s"
                30 -> "30s"
                60 -> "1m"
                300 -> "5m"
                else -> "Off"
            }

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(alpha = 0.2f))
                    .clickable { onOpenTimerDialog() }
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = timerLabel,
                    fontFamily = TitanOneFont,
                    fontSize = 14.sp,
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_left),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}