package com.example.comthupohaircut.presentation.screens.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.comthupohaircut.R
import com.example.comthupohaircut.presentation.screens.setting.components.SettingMenuItem
import com.example.comthupohaircut.ui.theme.ScreenGradientEnd
import com.example.comthupohaircut.ui.theme.ScreenGradientStart

@Composable
fun SettingScreen(
    onBackClick: () -> Unit
) {
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            ScreenGradientStart,
            ScreenGradientEnd
        )
    )
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_back),
                        contentDescription = null,
                        tint = Color.Unspecified
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(R.string.settings),
                    style = MaterialTheme.typography.titleLarge,
                )
                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.size(48.dp))
            }
            Spacer(modifier = Modifier.height(20.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SettingMenuItem(
                    title = stringResource(R.string.rate_app),
                    iconRes = R.drawable.ic_rate,
                    onClick = {}
                )
                SettingMenuItem(
                    title = stringResource(R.string.feed_back),
                    iconRes = R.drawable.ic_feedback,
                    onClick = {}
                )
                SettingMenuItem(
                    title = stringResource(R.string.privacy_policy),
                    iconRes = R.drawable.ic_policy,
                    onClick = {}
                )
            }
        }
    }
}

@Preview
@Composable
fun SettingScreenPreView() {
    SettingScreen(
        onBackClick = {}
    )
}