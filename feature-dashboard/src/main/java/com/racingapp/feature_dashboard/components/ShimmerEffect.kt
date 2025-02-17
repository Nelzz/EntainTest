/* (C) 2025. All rights reserved. */
package com.racingapp.feature_dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.racingapp.feature_dashboard.R
import com.valentinilk.shimmer.shimmer

@Composable
private fun ShimmerEffect(modifier: Modifier = Modifier) {
  Box(
    modifier =
      modifier
        .shimmer()
        .clip(RoundedCornerShape(8.dp))
        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)))
}

@Composable
fun ShimmerListItem() {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(2.dp),
    colors = CardDefaults.cardColors().copy(containerColor = Color.Transparent)) {
      Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        ShimmerEffect(modifier = Modifier.size(24.dp, 24.dp))
        Column(modifier = Modifier.weight(1f)) {
          Row(modifier = Modifier.padding(8.dp)) {
            ShimmerEffect(modifier = Modifier.weight(1f).height(24.dp))
            ShimmerEffect(modifier = Modifier.size(24.dp, 24.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
              Icons.AutoMirrored.Filled.KeyboardArrowRight,
              contentDescription = stringResource(R.string.go_to_details))
          }
          HorizontalDivider(modifier = Modifier.height(1.dp))
        }
      }
    }
}
