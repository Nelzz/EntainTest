/* (C) 2025. All rights reserved. */
package com.racingapp.ui_common.theme

import androidx.compose.ui.graphics.Color

data class CustomColors(
  val filterSelected: Color,
  val filterTextSelected: Color,
  val filterTextDeSelected: Color,
  val warningText: Color,
  val cardBackground: Color
)

val LightCustomColors =
  CustomColors(
    filterSelected = Color(0xFF0747C2),
    filterTextSelected = Color(0xFFD3E7EF),
    filterTextDeSelected = Color(0xFF171818),
    warningText = Color(0xFFD32F2F),
    cardBackground = Color(0xFFFFFFFF))

val DarkCustomColors =
  CustomColors(
    filterSelected = Color(0xFF1078F6),
    filterTextSelected = Color(0xFFE4E9EA),
    filterTextDeSelected = Color(0xFFE4E9EA),
    warningText = Color(0xFFDB4747),
    cardBackground = Color(0xFF212121))
