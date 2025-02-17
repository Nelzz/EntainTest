/* (C) 2025. All rights reserved. */
package com.racingapp.feature_dashboard.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.racingapp.feature_dashboard.SHIMMER_LIMIT_ITEMS

@Composable
fun LoadingContent() {
  Column(Modifier.padding(12.dp)) { repeat(SHIMMER_LIMIT_ITEMS) { ShimmerListItem() } }
}

@Preview(
  showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light Mode Preview")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark Mode Preview")
@Composable
private fun PreviewLoading() {
  LoadingContent()
}
