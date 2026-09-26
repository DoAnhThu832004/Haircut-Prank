package com.example.comthupohaircut.presentation.screens.splash

import android.window.SplashScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.comthupohaircut.R
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SplashScreen(
    viewModel: SplashViewModel,
    onNavigateNext: (navigationToIntro: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currentOnNavigateNext by rememberUpdatedState(onNavigateNext)
    LaunchedEffect(viewModel.effectFlow) {
        viewModel.effectFlow.collectLatest { effect ->
            when (effect) {
                is SplashUiEffect.NavigateNext -> {
                    currentOnNavigateNext(effect.navigationToIntro)
                }
            }
        }
    }
    SplashContent(
        uiState = uiState,
        onRetry = { viewModel.startPreparation() },
        modifier = modifier
    )
}
@Composable
fun SplashContent(
    uiState: SplashUiState,
    onRetry: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val mainComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.haircut_splash))
    val mainProgress by animateLottieCompositionAsState(
        composition = mainComposition,
        iterations = LottieConstants.IterateForever
    )
    val loadingComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading_haircut))
    val loadingProgress by animateLottieCompositionAsState(
        composition = loadingComposition,
        iterations = LottieConstants.IterateForever
    )
    Box(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LottieAnimation(
                composition = mainComposition,
                progress = { mainProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp, start = 24.dp, end = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            when(uiState) {
                is SplashUiState.Error -> {
                    Text(
                        text = uiState.errorMessage,
                        color = Color(0xFFFF6B6B),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                    Button(
                        onClick = onRetry
                    ) {
                        Text(
                            text = stringResource(R.string.retry)
                        )
                    }
                }
                is SplashUiState.Loading -> {
                    LottieAnimation(
                        composition = loadingComposition,
                        progress = { loadingProgress },
                        modifier = Modifier
                            .width(180.dp)
                            .height(80.dp)
                    )
//                    Text(
//                        text = "Do Anh Thu"
//                    )
                }
            }
        }
    }
}
@Preview(name = "Loading State", showBackground = true)
@Composable
fun SplashScreenLoadingPreview() {
    SplashContent(
        uiState = SplashUiState.Loading,
        onRetry = {}
    )
}
@Preview(name = "Error State", showBackground = true)
@Composable
fun SplashScreenErrorPreview() {
    SplashContent(
        uiState = SplashUiState.Error("Something went wrong"),
        onRetry = {}
    )
}