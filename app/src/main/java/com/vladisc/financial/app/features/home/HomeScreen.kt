package com.vladisc.financial.app.features.home

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.vladisc.financial.app.features.notifications.NotificationViewModel
import com.vladisc.financial.app.features.transactions.PushNotification
import com.vladisc.financial.app.features.transactions.PushNotificationsViewModel
import com.vladisc.financial.app.features.user.UserViewModel
import com.vladisc.financial.app.utils.DateUtils

@Composable
fun HomeScreen(
    navController: NavController,
    userViewModel: UserViewModel,
    pushNotificationsViewModel: PushNotificationsViewModel
) {
    val user by userViewModel.user

    val context = LocalContext.current
    val application = context.applicationContext as Application

    val notificationViewModel: NotificationViewModel = viewModel(
        factory = NotificationViewModel.provideFactory(application)
    )

    val notifications by notificationViewModel.notifications.collectAsState()
    var isUploading by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        userViewModel.getUser()
    }

    LaunchedEffect(user) {
        user?.let {
            val uid = it.uid
            if (!uid.isNullOrBlank()) {
                val prefs = context.getSharedPreferences("prefs", 0x0000)
                prefs.edit() { putString("user_id", uid) }
                notificationViewModel.setUserId(uid)
            }
        }
    }

    LaunchedEffect(notifications) {
        // Upload saved notifications
        if (notifications.isNotEmpty()) {
            println("notifications not empty")
            val pushNotifications = notifications.map {
                PushNotification(
                    timestamp = DateUtils.convertMillisToDate(
                        it.timestamp,
                        "yyyy-MM-dd'T'HH:mm:ss"
                    ),
                    body = it.body,
                    title = it.title,
                    id = it.id.toString()
                )
            }
            println(pushNotifications)
            isUploading = true
            pushNotificationsViewModel.postPushNotificationsInBatches(
                pushNotifications,
                onComplete = {
                    isUploading = false
                },
                onBatchComplete = {
                    notificationViewModel.deleteNotifications(it)
                })
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp, 24.dp, 24.dp, 32.dp)
            .windowInsetsPadding(WindowInsets.statusBars)
            .imePadding()
            .wrapContentSize(Alignment.Center),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            if (isUploading) {
                LinearProgressIndicator()
                Text("Uploading notifications…")
            }

        }
        TextButton(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = { navController.navigate("transactions") }) {
            Text(
                text = "Transactions",
            )
        }
        TextButton(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = { navController.navigate("select_apps") }) {
            Text(
                text = "Select banking app",
            )
        }
    }
}