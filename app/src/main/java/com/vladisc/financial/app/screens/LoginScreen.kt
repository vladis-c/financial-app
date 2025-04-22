package com.vladisc.financial.app.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.vladisc.financial.app.api.ApiClient
import com.vladisc.financial.app.storage.TokenStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun LoginScreen(navController: NavController) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    var canLogin by remember { mutableStateOf(false) }

    var fullWidth = Modifier
        .fillMaxWidth()
        .height(64.dp)

    LaunchedEffect(email, password) {
        canLogin = email.isNotBlank() && password.isNotBlank()
    }

    Column(
        Modifier
            .padding(16.dp)
            .windowInsetsPadding(WindowInsets.statusBars)
            .wrapContentSize(Alignment.Center)
    ) {
        Box(
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .padding(16.dp),
        ) {
            Text(
                text = "Log in",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        }

        OutlinedTextField(
            modifier = fullWidth,
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") })
        OutlinedTextField(
            modifier = fullWidth,
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            keyboardOptions = KeyboardOptions.Default.copy(
                autoCorrectEnabled = false,
                keyboardType = KeyboardType.NumberPassword,
            ),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                isLoading = true
                showError = false

                CoroutineScope(Dispatchers.IO).launch {
                    val success =
                        ApiClient.login(email, password)
                    withContext(Dispatchers.Main) {
                        isLoading = false
                        if (success) {
                            println("success")
//                        navController.navigate("home/$firstName/$lastName")

                        } else {
                            println("error")
                            showError = true
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            enabled = canLogin && !isLoading
        ) {
            Text(if (isLoading) "Logging in..." else "Log In")
        }
        Text(TokenStorage.getAccessToken()?: "sss")
    }
}
