package com.example.comthupohaircut.presentation.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.comthupohaircut.R
import com.example.comthupohaircut.domain.model.SoundCategory
import com.example.comthupohaircut.presentation.screens.home.components.CategoryGridItem
import com.example.comthupohaircut.presentation.screens.home.components.HomeTopBar
import com.example.comthupohaircut.ui.theme.ScreenGradientEnd
import com.example.comthupohaircut.ui.theme.ScreenGradientStart
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeScreen(
    onCategoryClick: (categoryName: String) -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val currentOnCategoryClick by rememberUpdatedState(onCategoryClick)
        val currentOnSettingsClick by rememberUpdatedState(onSettingsClick)
        LaunchedEffect(viewModel.effectFlow) {
            viewModel.effectFlow.collectLatest { effect ->
                when (effect) {
                    is HomeUiEffect.NavigateToListSound -> {
                        currentOnCategoryClick(effect.categoryName)
                    }
                    is HomeUiEffect.NavigateToSettings -> {
                        currentOnSettingsClick()
                    }
                }
            }
        }
        HomeScreenContent(
            uiState = uiState,
            onCategoryClick = { category -> viewModel.onCategoryClick(category) },
            onSettingsClick = { viewModel.onSettingsClick() },
            modifier = modifier
        )
    }
}
@Composable
fun HomeScreenContent(
    uiState: HomeUiState,
    onCategoryClick: (SoundCategory) -> Unit,
    onSettingsClick: () -> Unit,
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
        Column(modifier = Modifier.fillMaxSize()) {
            HomeTopBar(onSettingsClick = onSettingsClick)
            if(uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            } else if(uiState.errorMessage != null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = uiState.errorMessage,
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(
                        items = uiState.categories,
                        key = { it.id }
                    ) { category ->
                        CategoryGridItem(
                            category = category,
                            onClick = { onCategoryClick(category) }
                        )
                    }
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
private fun HomeScreenContentPreview() {
    val mockCategories = listOf(
        SoundCategory(1, "Hair Clipper", isNew = true),
        SoundCategory(2, "Air Horn", isNew = false),
        SoundCategory(3, "Fart", isNew = false),
        SoundCategory(4, "Gun", isNew = false),
        SoundCategory(5, "Car", isNew = false),
        SoundCategory(6, "Meme", isNew = false)
    )
    HomeScreenContent(
        uiState = HomeUiState(isLoading = false, categories = mockCategories),
        onCategoryClick = {},
        onSettingsClick = {}
    )
}