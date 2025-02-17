/* (C) 2025. All rights reserved. */
package com.racingapp.feature_dashboard.mapper

import com.racingapp.domain.models.Category
import com.racingapp.feature_dashboard.R
import com.racingapp.feature_dashboard.model.CategoryUI
import javax.inject.Inject

class CategoryUiMapper @Inject constructor() {
  fun fromDomain(category: Category): CategoryUI {
    return when (category) {
      Category.HORSE ->
        CategoryUI(R.string.horse_racing, category.id, com.racingapp.ui_common.R.drawable.noun_race)
      Category.HARNESS ->
        CategoryUI(
          R.string.harness_racing, category.id, com.racingapp.ui_common.R.drawable.noun_chariot)
      Category.GREYHOUND ->
        CategoryUI(
          R.string.greyhound_racing, category.id, com.racingapp.ui_common.R.drawable.noun_greyhound)
    }
  }
}
