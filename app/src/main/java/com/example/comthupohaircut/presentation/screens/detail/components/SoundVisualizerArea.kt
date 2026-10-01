package com.example.comthupohaircut.presentation.screens.detail.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.comthupohaircut.R
import com.example.comthupohaircut.domain.model.Sound
import com.example.comthupohaircut.presentation.screens.home.CategoryResourceMapper
import com.example.comthupohaircut.ui.theme.TitanOneFont
import java.io.File

@Composable
fun SoundVisualizerArea(
    sound: Sound?,
    isPlaying: Boolean,
    isCountingDown: Boolean,
    countdownRemaining: Int,
    isVibrationEnabled: Boolean,
    onTogglePlay: () -> Unit,
    onToggleVibrate: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val categoryKey = sound?.idCategory?.lowercase()?.replace(" ", "_").orEmpty()
    val localIconRes = remember(categoryKey) { CategoryResourceMapper.getLocalIcon(categoryKey) }

    // Hiệu ứng sóng vòng tròn Ripple Wave khi đang phát
    val infiniteTransition = rememberInfiniteTransition(label = "WaveAnimation")
    val waveScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.35f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "waveScale"
    )
    val waveAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "waveAlpha"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isCountingDown) {
            Row(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White)
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.time_out),
                    contentDescription = null,
                    modifier = Modifier.size(26.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                val minutes = countdownRemaining / 60
                val seconds = countdownRemaining % 60
                Text(
                    text = String.format("%02d:%02d", minutes, seconds),
                    fontFamily = TitanOneFont,
                    fontSize = 15.sp,
                    color = Color(0xFF1B0A3A)
                )
            }
        }
        IconButton(
            onClick = onToggleVibrate,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(56.dp)
        ) {
            Icon(
                painter = painterResource(id = if (isVibrationEnabled) R.drawable.ic_ring else R.drawable.ic_unring),
                contentDescription = "Vibrate",
                tint = Color.Unspecified
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(240.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onTogglePlay
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isPlaying) {
                    Box(
                        modifier = Modifier
                            .size(220.dp)
                            .graphicsLayer {
                                scaleX = waveScale
                                scaleY = waveScale
                                alpha = waveAlpha
                            }
                            .clip(CircleShape)
                            .background(Color(0xFF966DCB))
                    )
                }

                if (localIconRes != null) {
                    Image(
                        painter = painterResource(id = localIconRes),
                        contentDescription = sound?.name,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(190.dp)
                    )
                } else if (!sound?.iconPath.isNullOrEmpty()) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(File(sound!!.iconPath))
                            .crossfade(true)
                            .build(),
                        contentDescription = sound.name,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(190.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            val buttonColor = if (isCountingDown) Color(0xFFE53935) else Color(0xFF8A38F5)
            val iconRes = when {
                isCountingDown -> R.drawable.ic_pause
                isPlaying -> R.drawable.ic_pause
                else -> R.drawable.ic_play
            }
            val buttonText = when {
                isCountingDown -> "CANCEL"
                isPlaying -> "PAUSE"
                else -> "PLAY"
            }

            Row(
                modifier = Modifier
                    .width(160.dp)
                    .height(48.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(buttonColor)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(bounded = true, color = Color.White),
                        onClick = onTogglePlay
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = buttonText,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = buttonText,
                    fontFamily = TitanOneFont,
                    fontSize = 17.sp,
                    color = Color.White
                )
            }
        }
    }
}