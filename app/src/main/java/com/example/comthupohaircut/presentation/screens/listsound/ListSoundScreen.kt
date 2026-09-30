package com.example.comthupohaircut.presentation.screens.listsound

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.comthupohaircut.domain.model.Sound
import com.example.comthupohaircut.presentation.screens.listsound.components.ListSoundTopBar
import com.example.comthupohaircut.presentation.screens.listsound.components.SoundGridItem
import com.example.comthupohaircut.ui.theme.ScreenGradientEnd
import com.example.comthupohaircut.ui.theme.ScreenGradientStart
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ListSoundScreen(
    onBackClick: () -> Unit,
    onSoundClick: (Sound) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ListSoundViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.effectFlow) {
        viewModel.effectFlow.collectLatest { effect ->
            when (effect) {
                is ListSoundEffect.NavigateBack -> onBackClick()
                is ListSoundEffect.NavigateToDetail -> onSoundClick(effect.sound)
            }
        }
    }

    ListSoundContent(
        uiState = uiState,
        onBackClick = viewModel::onBackClick,
        onSoundClick = viewModel::onSoundClick,
        modifier = modifier
    )
}

@Composable
fun ListSoundContent(
    uiState: ListSoundState,
    onBackClick: () -> Unit,
    onSoundClick: (Sound) -> Unit,
    modifier: Modifier = Modifier
) {
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
        when (uiState) {
            is ListSoundState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            }
            is ListSoundState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = uiState.message,
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            is ListSoundState.Success -> {
                Column(modifier = Modifier.fillMaxSize()) {
                    ListSoundTopBar(
                        title = uiState.categoryName,
                        onBackClick = onBackClick
                    )

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(
                            items = uiState.sounds,
                            key = { it.pathSound }
                        ) { sound ->
                            SoundGridItem(
                                sound = sound,
                                onClick = { onSoundClick(sound) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ListSoundContentPreview() {
    val mockSounds = listOf(
        Sound(id = 1, idCategory = "Hair Clipper", name = "Sound 1", isNew = true),
        Sound(id = 2, idCategory = "Hair Clipper", name = "Sound 2", isNew = false),
        Sound(id = 3, idCategory = "Hair Clipper", name = "Sound 3", isNew = false),
        Sound(id = 4, idCategory = "Hair Clipper", name = "Sound 4", isNew = false)
    )
    ListSoundContent(
        uiState = ListSoundState.Success("Hair Clipper", mockSounds),
        onBackClick = {},
        onSoundClick = {}
    )
}