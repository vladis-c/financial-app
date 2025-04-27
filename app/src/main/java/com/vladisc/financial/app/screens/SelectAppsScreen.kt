package com.vladisc.financial.app.screens

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.vladisc.financial.app.components.CheckboxRow
import androidx.core.content.edit

@SuppressLint("QueryPermissionsNeeded")
@Composable
fun SelectAppsScreen(
    navController: NavController,
) {
    val context = LocalContext.current
    val packageManager = context.packageManager
    val prefs = context.getSharedPreferences("prefs", 0)

    // Get all installed apps
    val mainIntent = Intent(Intent.ACTION_MAIN, null)
    mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)

    val apps = remember {
        packageManager.queryIntentActivities(mainIntent, 0)
            .map { it.activityInfo.applicationInfo }
            .sortedBy { packageManager.getApplicationLabel(it).toString() }
    }

    // Load selected apps from SharedPreferences
    val selectedApps = remember { mutableStateListOf<String>().apply {
        addAll(prefs.getStringSet("allowed_apps", emptySet()) ?: emptySet())
    } }

    LazyColumn(
        modifier = Modifier
            .padding(24.dp, 24.dp, 24.dp, 32.dp)
            .windowInsetsPadding(WindowInsets.statusBars)
            .imePadding() // Handles keyboard insets
            .wrapContentSize(Alignment.Center),
    ) {
        items(apps) { app ->
            val appName = packageManager.getApplicationLabel(app).toString()
            val appIcon = packageManager.getApplicationIcon(app)
            val isSelected = selectedApps.contains(app.packageName)

            CheckboxRow(
                appName = appName,
                appIcon = appIcon,
                packageName = app.packageName,
                isSelected = isSelected,
                onCheckedChange = { checked ->
                    if (checked) {
                        selectedApps.add(app.packageName)
                    } else {
                        selectedApps.remove(app.packageName)
                    }
                    // Update SharedPreferences immediately
                    prefs.edit() { putStringSet("allowed_apps", selectedApps.toSet()) }
                }
            )
        }
    }
}