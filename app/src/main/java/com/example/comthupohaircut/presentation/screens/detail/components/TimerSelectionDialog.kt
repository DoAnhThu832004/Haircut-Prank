package com.example.comthupohaircut.presentation.screens.detail.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.comthupohaircut.ui.theme.TitanOneFont

@Composable
fun TimerSelectionDialog(
    selectedSeconds: Int,
    onSelect: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    val options = listOf(
        0 to "Off",
        5 to "5s",
        10 to "10s",
        30 to "30s",
        60 to "1 minute",
        300 to "5 minutes"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(20.dp),
        containerColor = Color(0xFF2E0854),
        title = {
            Text(
                text = "Play After",
                fontFamily = TitanOneFont,
                fontSize = 18.sp,
                color = Color.White
            )
        },
        text = {
            Column {
                options.forEach { (seconds, label) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onSelect(seconds)
                                onDismiss()
                            }
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedSeconds == seconds,
                            onClick = {
                                onSelect(seconds)
                                onDismiss()
                            },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color(0xFFC048FB),
                                unselectedColor = Color.White.copy(alpha = 0.6f)
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = label,
                            fontFamily = TitanOneFont,
                            fontSize = 15.sp,
                            color = Color.White
                        )
                    }
                }
            }
        },
        confirmButton = {}
    )
}