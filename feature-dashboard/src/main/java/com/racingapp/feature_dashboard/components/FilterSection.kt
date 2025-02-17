/* (C) 2025. All rights reserved. */
package com.racingapp.feature_dashboard.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.racingapp.feature_dashboard.R
import com.racingapp.feature_dashboard.model.CategoryUI
import com.racingapp.ui_common.components.AppText
import com.racingapp.ui_common.theme.LocalCustomColors

@Composable
fun FilterSection(
  categories: List<CategoryUI>,
  selectedCategories: Set<CategoryUI>,
  onFilterSelected: (CategoryUI) -> Unit
) {

  LazyRow(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    contentPadding = PaddingValues(horizontal = 16.dp)) {
      items(items = categories.toList()) { category ->
        Row {
          CategoryChip(
            category = category,
            isSelected = selectedCategories.contains(category),
            onToggle = onFilterSelected)
        }
      }
    }
}

@Composable
private fun CategoryChip(
  category: CategoryUI,
  isSelected: Boolean,
  onToggle: (CategoryUI) -> Unit
) {
  val context = LocalContext.current
  val textColorSelected =
    if (isSelected) {
      LocalCustomColors.current.filterTextSelected
    } else {
      LocalCustomColors.current.filterTextDeSelected
    }
  FilterChip(
    modifier = Modifier.testTag("test-category-chip-${category.id}"),
    selected = isSelected,
    onClick = { onToggle(category) },
    label = { AppText(context.getString(category.name), color = textColorSelected) },
    colors =
      FilterChipDefaults.filterChipColors()
        .copy(
          selectedContainerColor = LocalCustomColors.current.filterSelected,
          labelColor = LocalCustomColors.current.filterTextSelected,
        ),
    leadingIcon = {
      category.iconRes?.let {
        Icon(
          painterResource(it),
          modifier = Modifier.size(24.dp),
          contentDescription = context.getString(category.name))
      }
    })
}

@Preview(
  showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light Mode Preview")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark Mode Preview")
@Composable
fun PreviewFilterSection() {

  val sampleCategories =
    listOf(
      CategoryUI(R.string.horse_racing, "1", com.racingapp.ui_common.R.drawable.noun_none),
      CategoryUI(R.string.harness_racing, "2", com.racingapp.ui_common.R.drawable.noun_race),
      CategoryUI(R.string.greyhound_racing, "3"),
    )

  FilterSection(
    categories = sampleCategories,
    selectedCategories = setOf(sampleCategories[0]),
    onFilterSelected = {})
}
