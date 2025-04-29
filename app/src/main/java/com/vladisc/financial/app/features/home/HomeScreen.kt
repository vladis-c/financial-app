package com.vladisc.financial.app.features.home

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.vladisc.financial.app.features.notifications.NotificationViewModel
import com.vladisc.financial.app.features.user.UserViewModel

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

    val listState = rememberLazyListState()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp, 24.dp, 24.dp, 32.dp)
            .windowInsetsPadding(WindowInsets.statusBars)
            .verticalScroll(scrollState)
            .imePadding()
            .wrapContentSize(Alignment.Center),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextButton(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = { navController.navigate("transactions") }) {
            Text(
                text = "Transactions",
            )
        }
    }
//    //TODO: fix this screen view
//    Box(
//        modifier = Modifier
//            .padding(24.dp, 24.dp, 24.dp, 32.dp)
//            .windowInsetsPadding(WindowInsets.statusBars)
//            .imePadding()
//            .wrapContentSize(Alignment.Center)
//    ) {
//
//        Box(
//            modifier = Modifier.fillMaxSize()
//        ) {
//            if (user != null) {
//                Text("Welcome, ${user?.firstName ?: "User"}!")
//            } else {
//                Text("Loading...")
//            }
//        }
//        TextButton(
//            modifier = Modifier
////            .align(alignment = Alignment.CenterHorizontally)
//                .fillMaxWidth()
//                .height(48.dp),
//            onClick = { navController.navigate("select_apps") }) {
//            Text(
//                text = "Select banking app",
//            )
//        }
//        LazyColumn(
//            state = listState, modifier = Modifier
//                .fillMaxWidth()
//                .padding(0.dp, 100.dp, 0.dp, 0.dp),
//            verticalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            items(notifications) { notification ->
//                NotificationItem(
//                    notification.title ?: "",
//                    notification.body ?: "",
//                    notification.packageName,
//                )
//            }
//        }
//    }

    // Detect when scrolled to bottom
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo }
            .collect { layoutInfo ->
                val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
                if (lastVisibleItem != null && lastVisibleItem.index >= notifications.size - 5) {
                    // TODO paging
                }
            }
    }
}