package com.example.comthupohaircut.presentation.screens.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.comthupohaircut.presentation.screens.detail.components.*
import com.example.comthupohaircut.ui.theme.ScreenGradientEnd
import com.example.comthupohaircut.ui.theme.ScreenGradientStart
import kotlinx.coroutines.flow.collectLatest

@Composable
fun DetailSoundScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetailSoundViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.effectFlow) {
        viewModel.effectFlow.collectLatest { effect ->
            when (effect) {
                is DetailSoundUiEffect.NavigateBack -> onBackClick()
            }
        }
    }

    DetailSoundContent(
        uiState = uiState,
        onBackClick = viewModel::onBackClick,
        onTogglePlay = viewModel::onTogglePlay,
        onToggleLoop = viewModel::onToggleLoop,
        onToggleVibrate = viewModel::onToggleVibration,
        onToggleFavorite = viewModel::onToggleFavorite,
        onSelectTimer = viewModel::onSelectTimer,
        onSelectSound = viewModel::onSelectSound,
        modifier = modifier
    )
}

@Composable
fun DetailSoundContent(
    uiState: DetailSoundUiState,
    onBackClick: () -> Unit,
    onTogglePlay: () -> Unit,
    onToggleLoop: (Boolean) -> Unit,
    onToggleVibrate: () -> Unit,
    onToggleFavorite: () -> Unit,
    onSelectTimer: (Int) -> Unit,
    onSelectSound: (com.example.comthupohaircut.domain.model.Sound) -> Unit,
    modifier: Modifier = Modifier
) {
    var showTimerDialog by remember { mutableStateOf(false) }

    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            ScreenGradientStart,
            ScreenGradientEnd
        )
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(gradientBrush)
            .statusBarsPadding()
    ) {
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                DetailSoundTopBar(
                    title = uiState.currentSound?.name?.replace(".mp3", "").orEmpty(),
                    isFavorite = uiState.isFavorite,
                    onBackClick = onBackClick,
                    onFavoriteClick = onToggleFavorite
                )

                SoundVisualizerArea(
                    sound = uiState.currentSound,
                    isPlaying = uiState.isPlaying,
                    isCountingDown = uiState.isCountingDown,
                    countdownRemaining = uiState.countdownRemainingSeconds,
                    isVibrationEnabled = uiState.isVibrationEnabled,
                    onTogglePlay = onTogglePlay,
                    onToggleVibrate = onToggleVibrate,
                    modifier = Modifier.weight(1f)
                )

                SoundControlsSection(
                    isLooping = uiState.isLooping,
                    selectedTimerSeconds = uiState.selectedTimerSeconds,
                    onToggleLoop = onToggleLoop,
                    onOpenTimerDialog = { showTimerDialog = true },
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                OtherSoundsRow(
                    sounds = uiState.otherSounds,
                    currentSoundPath = uiState.currentSound?.pathSound.orEmpty(),
                    onSoundSelected = onSelectSound,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }
        }

        // Hộp thoại chọn hẹn giờ
        if (showTimerDialog) {
            TimerSelectionDialog(
                selectedSeconds = uiState.selectedTimerSeconds,
                onSelect = onSelectTimer,
                onDismiss = { showTimerDialog = false }
            )
        }
    }
}