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
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.vladisc.financial.app.api.ApiClient
import com.vladisc.financial.app.components.DatePicker
import com.vladisc.financial.app.utils.DateUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun SignUpScreen(navController: NavController) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var company by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var dateOfBirth by rememberSaveable { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }

    var fullWidth = Modifier
        .fillMaxWidth()
        .height(64.dp)

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
                text = "Sign up",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        }
        OutlinedTextField(
            modifier = fullWidth,
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("First Name") })
        OutlinedTextField(
            modifier = fullWidth,
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Last Name") })
        OutlinedTextField(
            modifier = fullWidth,
            value = company,
            onValueChange = { company = it },
            label = { Text("Company") })
        DatePicker(
            onDateSelected = {
                dateOfBirth = DateUtils.convertDateFromPatternToISO(it, "d.M.yyyy")
            },
            label = "Date of Birth",
        )
        OutlinedTextField(
            modifier = fullWidth,
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") })
        OutlinedTextField(
            modifier = fullWidth,
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") })

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                isLoading = true
                showError = false

                CoroutineScope(Dispatchers.IO).launch {
                    val success =
                        ApiClient.signUp(firstName, lastName, company, email, password, dateOfBirth)
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
            enabled = !isLoading
        ) {
            Text(if (isLoading) "Signing up..." else "Sign Up")
        }

        if (showError) {
            Text("Sign up failed", color = Color.Red)
        }

        Spacer(modifier = Modifier.height(8.dp))
        TextButton(
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .fillMaxWidth()
                .height(36.dp),
            onClick = { navController.navigate("login") }) {
            Text(
                "Already have an account? Log in",

                )
        }
    }
}
