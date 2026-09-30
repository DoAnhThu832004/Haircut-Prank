package com.example.comthupohaircut.presentation.screens.favorite

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.comthupohaircut.domain.model.Sound
import com.example.comthupohaircut.presentation.screens.favorite.components.FavoriteEmptyView
import com.example.comthupohaircut.presentation.screens.favorite.components.FavoriteItemCard
import com.example.comthupohaircut.presentation.screens.favorite.components.FavoriteTopBar

@Composable
fun FavoriteScreen(
    onSoundClick: (Sound) -> Unit = {},
    onSettingsClick: () -> Unit = {},
    viewModel: FavoriteViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    FavoriteContent(
        uiState = uiState,
        onToggleFavorite = viewModel::onToggleFavorite,
        onSoundClick = onSoundClick,
        onSettingsClick = onSettingsClick,
        modifier = modifier
    )
}

@Composable
fun FavoriteContent(
    uiState: FavoriteUiState,
    onToggleFavorite: (Sound) -> Unit,
    onSoundClick: (Sound) -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        FavoriteTopBar(
            onSettingClick = onSettingsClick
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            when (uiState) {
                is FavoriteUiState.Loading -> {
                    CircularProgressIndicator(
                        color = Color.White
                    )
                }
                is FavoriteUiState.Empty -> {
                    FavoriteEmptyView()
                }
                is FavoriteUiState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(vertical = 12.dp)
                    ) {
                        items(
                            items = uiState.sounds,
                            key = { it.pathSound }
                        ) { sound ->
                            FavoriteItemCard(
                                sound = sound,
                                onClick = { onSoundClick(sound) },
                                onToggleFavorite = { onToggleFavorite(sound) }
                            )
                        }
                    }
                }
            }
        }
    }
}