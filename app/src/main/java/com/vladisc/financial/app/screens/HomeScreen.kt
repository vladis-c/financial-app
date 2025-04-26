package com.vladisc.financial.app.screens

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.vladisc.financial.app.api.UserViewModel
import com.vladisc.financial.app.components.NotificationItem
import com.vladisc.financial.app.services.NotificationViewModel
import androidx.core.content.edit

@Composable
fun HomeScreen(
    navController: NavController,
    userViewModel: UserViewModel,
) {
    val user by userViewModel.user

    val context = LocalContext.current
    val application = context.applicationContext as Application

    val notificationViewModel: NotificationViewModel = viewModel(
        factory = NotificationViewModel.provideFactory(application)
    )

    val notifications by notificationViewModel.notifications.collectAsState()

    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        userViewModel.getUser()
    }

    LaunchedEffect(user) {
        user?.let {
            val uid = it.uid
            println("Getting notifications effect $uid")
            if (!uid.isNullOrBlank()) {
                // TODO: remember to remove this user id when logging out
                val prefs = context.getSharedPreferences("prefs", 0x0000)
                prefs.edit() { putString("user_id", uid) }
                notificationViewModel.setUserId(uid)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp, 24.dp, 24.dp, 32.dp)
            .windowInsetsPadding(WindowInsets.statusBars)
            .verticalScroll(scrollState)
            .imePadding() // Handles keyboard insets
            .wrapContentSize(Alignment.Center),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (user != null) {
                Text("Welcome, ${user?.firstName ?: "User"}!")
            } else {
                Text("Loading...")
            }
        }
        notifications.forEach { notification ->
            NotificationItem(
                title = notification.title ?: "",
                body = notification.body ?: ""
            )
        }
    }
}