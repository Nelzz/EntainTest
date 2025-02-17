/* (C) 2025. All rights reserved. */
package com.racingapp.feature_dashboard.components

import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.racingapp.feature_dashboard.R
import com.racingapp.feature_dashboard.model.DashboardModel
import com.racingapp.ui_common.components.AppText

@Composable
fun RaceItem(dashboardModel: DashboardModel) {
  val context = LocalContext.current
  Card(
    modifier = Modifier.fillMaxWidth().clickable {},
    shape = RoundedCornerShape(2.dp),
    colors = CardDefaults.cardColors().copy(containerColor = Color.Transparent)) {
      Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        val iconCategory =
          dashboardModel.categoryUI.iconRes ?: com.racingapp.ui_common.R.drawable.noun_none
        Icon(
          painterResource(iconCategory),
          modifier = Modifier.size(24.dp),
          contentDescription = context.getString(dashboardModel.categoryUI.name))

        Column(modifier = Modifier.weight(1f)) {
          Row(modifier = Modifier.padding(8.dp)) {
            AppText(
              context.getString(
                R.string.meeting_race, dashboardModel.meetingName, dashboardModel.raceNumber),
              modifier = Modifier.weight(1f))

            CountdownTimer(dashboardModel.advertiseStartInSeconds)
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
              Icons.AutoMirrored.Filled.KeyboardArrowRight,
              contentDescription = context.getString(R.string.go_to_details))
          }
          HorizontalDivider(modifier = Modifier.height(1.dp))
        }
      }
    }
}
