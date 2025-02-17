/* (C) 2025. All rights reserved. */
package com.racingapp.feature_dashboard.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.SnackbarData
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.racingapp.feature_dashboard.R
import com.racingapp.ui_common.components.AppText

@Composable
fun SnackBarContent(snackbarData: SnackbarData, onAction: () -> Unit) {
  Box(modifier = Modifier.fillMaxWidth()) {
    Card(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
      Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
        Spacer(modifier = Modifier.width(8.dp))
        AppText(snackbarData.visuals.message, modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.width(8.dp))
        Button(
          onClick = {
            onAction()
            snackbarData.dismiss()
          }) {
            AppText(text = snackbarData.visuals.actionLabel ?: stringResource(R.string.retry))
          }
      }
    }
  }
}
