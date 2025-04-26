package com.vladisc.financial.app.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.vladisc.financial.app.api.UserViewModel

@Composable
fun HomeScreen(navController: NavController, userViewModel: UserViewModel = viewModel()) {
    val user by userViewModel.user

    LaunchedEffect(Unit) {
        userViewModel.getUser()
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (user != null) {
            Text("Welcome, ${user?.firstName ?: "User"}!")
        } else {
            Text("Loading...")
        }
    }
}