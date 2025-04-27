package com.vladisc.financial.app.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NotificationItem(
    title: String,
    body: String,
    packageName: String,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = title)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = body)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = packageName)
        HorizontalDivider(modifier = Modifier.padding(top = 8.dp))
    }
}