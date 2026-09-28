package com.example.comthupohaircut.presentation.screens.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.comthupohaircut.R
import com.example.comthupohaircut.domain.model.SoundCategory
import com.example.comthupohaircut.presentation.screens.home.CategoryResourceMapper
import java.io.File

@Composable
fun CategoryGridItem(
    category: SoundCategory,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val key = category.normalizedKey

    val localBgRes = remember(key) { CategoryResourceMapper.getLocalBackground(key) }
    val localIconRes = remember(key) { CategoryResourceMapper.getLocalIcon(key) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(170f / 191f)
            .clip(RoundedCornerShape(16.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = true, color = Color.White.copy(alpha = 0.3f)),
                onClick = onClick
            )
    ) {
        if(localBgRes != null) {
            Image(
                painter = painterResource(localBgRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(File(category.backgroundPath))
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )
        }
        if (localIconRes != null) {
            Image(
                painter = painterResource(id = localIconRes),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.Center)
                    .offset(y = (-10).dp)
            )
        } else if (category.iconImage.isNotEmpty()) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(File(category.iconImage))
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.Center)
                    .offset(y = (-10).dp)
            )
        }
        if (category.isNew) {
            Image(
                painter = painterResource(id = R.drawable.tag_new),
                contentDescription = "NEW Category",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 2.dp, end = 2.dp)
                    .width(42.dp)
            )
        }
        Text(
            text = category.name,
            style = MaterialTheme.typography.titleMedium.copy(fontSize = 17.sp),
            color = Color.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = 8.dp, vertical = 12.dp)
        )
    }
}
@Preview(name = "Normal Item")
@Composable
private fun CategoryGridItemPreview() {
    CategoryGridItem(
        category = SoundCategory(
            id = 1,
            name = "Air Horn",
            isNew = false
        ),
        onClick = {},
        modifier = Modifier.width(170.dp)
    )
}
@Preview(name = "NEW Tag Item")
@Composable
private fun CategoryGridItemNewPreview() {
    CategoryGridItem(
        category = SoundCategory(
            id = 2,
            name = "Hair Clipper",
            isNew = true
        ),
        onClick = {},
        modifier = Modifier.width(170.dp)
    )
}

