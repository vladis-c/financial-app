package com.vladisc.financial.app.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.vladisc.financial.app.api.UserViewModel
import com.vladisc.financial.app.components.CheckboxRow
import androidx.core.content.edit

@SuppressLint("QueryPermissionsNeeded")
@Composable
fun SelectAppsScreen(
    navController: NavController,
    userViewModel: UserViewModel,
) {
    val context = LocalContext.current
    val packageManager = context.packageManager
    val prefs = context.getSharedPreferences("prefs", 0)

    // Get all installed apps
    val apps = remember {
        packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
            .sortedBy { packageManager.getApplicationLabel(it).toString() }
    }

    // Load selected apps from SharedPreferences
    val selectedApps = remember { mutableStateListOf<String>().apply {
        addAll(prefs.getStringSet("allowed_apps", emptySet()) ?: emptySet())
    } }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
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