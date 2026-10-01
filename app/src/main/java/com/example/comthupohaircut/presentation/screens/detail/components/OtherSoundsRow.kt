package com.example.comthupohaircut.presentation.screens.detail.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.comthupohaircut.domain.model.Sound
import com.example.comthupohaircut.presentation.screens.home.CategoryResourceMapper
import com.example.comthupohaircut.ui.theme.TitanOneFont
import java.io.File

@Composable
fun OtherSoundsRow(
    sounds: List<Sound>,
    currentSoundPath: String,
    onSoundSelected: (Sound) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Others sound",
            fontFamily = TitanOneFont,
            fontSize = 16.sp,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 6.dp)
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = sounds,
                key = { it.pathSound }
            ) { sound ->
                val isSelected = sound.pathSound == currentSoundPath
                val categoryKey = sound.idCategory.lowercase().replace(" ", "_")
                val localIconRes = remember(categoryKey) { CategoryResourceMapper.getLocalIcon(categoryKey) }

                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.White.copy(alpha = if (isSelected) 0.35f else 0.15f))
                        .then(
                            if (isSelected) Modifier.border(2.dp, Color(0xFFC048FB), RoundedCornerShape(14.dp))
                            else Modifier
                        )
                        .clickable { onSoundSelected(sound) },
                    contentAlignment = Alignment.Center
                ) {
                    if (localIconRes != null) {
                        Image(
                            painter = painterResource(id = localIconRes),
                            contentDescription = sound.name,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(46.dp)
                        )
                    } else if (sound.iconPath.isNotEmpty()) {
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(File(sound.iconPath))
                                .crossfade(true)
                                .build(),
                            contentDescription = sound.name,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(46.dp)
                        )
                    }
                }
            }
        }
    }
}