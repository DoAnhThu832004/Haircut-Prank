package com.example.comthupohaircut.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.comthupohaircut.R

val TitanOneFont = FontFamily(
    Font(R.font.titan_one, FontWeight.Normal)
)
val Typography = Typography(
    bodyMedium = TextStyle(
        fontFamily = TitanOneFont,
        fontSize = 14.sp,
        color = ColorOnGradientSecondary
    ),
    titleLarge = TextStyle(
        fontFamily = TitanOneFont,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        color = ColorOnGradient
    ),
    titleMedium = TextStyle(
        fontFamily = TitanOneFont,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        color = ColorOnGradient
    )
)