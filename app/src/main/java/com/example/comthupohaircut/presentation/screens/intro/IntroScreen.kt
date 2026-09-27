package com.example.comthupohaircut.presentation.screens.intro

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.comthupohaircut.R
import com.example.comthupohaircut.ui.theme.ScreenGradientEnd
import com.example.comthupohaircut.ui.theme.ScreenGradientStart
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class IntroPageData(val imgRes: Int, val titleRes: Int)

@Composable
fun IntroScreen(
    viewModel: IntroViewModel,
    onNavigateToMain: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentOnNavigateToMain by rememberUpdatedState(onNavigateToMain)
    LaunchedEffect(viewModel.effectFlow) {
        viewModel.effectFlow.collectLatest { effect ->
            when (effect) {
                is IntroUiEffect.NavigateToMain -> {
                    currentOnNavigateToMain()
                }
            }
        }
    }
    IntroContent(
        onFinished = { viewModel.onFinishIntro() },
        modifier = modifier
    )
}
@Composable
fun IntroContent(
    onFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pages = listOf(
        IntroPageData(R.drawable.intro_1, R.string.intro_title_1),
        IntroPageData(R.drawable.intro_2, R.string.intro_title_2),
        IntroPageData(R.drawable.intro_3, R.string.intro_title_3)
    )
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            ScreenGradientStart,
            ScreenGradientEnd
        )
    )
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()
    val isLastPage = pagerState.currentPage == pages.lastIndex
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush)
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    if(!isLastPage) {
                        Button(
                            onClick = onFinished,
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            shape = RoundedCornerShape(24.dp),
                            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.skip),
                                color = Color(0xFFE8962B),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            },
            bottomBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        repeat(pages.size) { index ->
                            val isSelected = pagerState.currentPage == index
                            val width by animateDpAsState(
                                targetValue = if(isSelected) 22.dp else 8.dp,
                                label = ""
                            )
                            Box(
                                modifier = Modifier
                                    .height(8.dp)
                                    .width(width)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(if(isSelected) Color.White else Color.White.copy(alpha = 0.4f))
                            )
                        }
                    }
                    if(isLastPage) {
                        Button(
                            onClick = onFinished,
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            shape = RoundedCornerShape(24.dp),
                            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.start),
                                color = Color(0xFFE8962B),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    } else {
                        Button(
                            onClick = {
                                scope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            shape = RoundedCornerShape(24.dp),
                            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.next),
                                color = Color(0xFFE8962B),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
//                    IconButton(
//                        onClick = {
//                            scope.launch {
//                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
//                            }
//                        },
//                        modifier = Modifier
//                            .size(56.dp)
//                            .rotate(180f)
//                    ) {
//                        Icon(
//                            painter = painterResource(R.drawable.ic_back),
//                            contentDescription = null,
//                            tint = Color.Unspecified
//                        )
//                    }
                    }
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { index ->
                    val page = pages[index]
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(page.imgRes),
                            contentDescription = null,
                            modifier = Modifier
                                .size(350.dp)
                                .padding(bottom = 32.dp)
                        )
                        Text(
                            text = stringResource(page.titleRes),
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}
@Preview
@Composable
fun IntroScreenPreview() {
    IntroContent(
        onFinished = {}
    )
}