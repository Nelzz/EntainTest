/* (C) 2025. All rights reserved. */
package com.racingapp.ui_common.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun AppText(
  text: String,
  modifier: Modifier = Modifier,
  color: Color = MaterialTheme.colorScheme.onBackground,
  fontWeight: FontWeight? = null,
  fontSize: TextUnit = 14.sp
) {
  Text(text, modifier, color = color, fontWeight = fontWeight, fontSize = fontSize)
}
