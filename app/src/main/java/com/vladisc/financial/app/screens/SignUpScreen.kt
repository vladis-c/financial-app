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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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

enum class FormFieldKey {
    FirstName, LastName, Company, Email, Password, ConfirmPassword, DateOfBirth
}

data class FormField(
    val key: FormFieldKey,
    val label: String,
    val isSecure: Boolean = false,
    val state: MutableState<String>
)

@Composable
fun SignUpScreen(navController: NavController) {
    var isLoading by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    var canSignup by remember { mutableStateOf(false) }

    val fullWidth = Modifier
        .fillMaxWidth()
        .height(64.dp)

    val formFields = remember {
        mapOf(
            "firstName" to FormField(
                key = FormFieldKey.FirstName,
                label = "First Name",
                state = mutableStateOf("")
            ),
            "lastName" to FormField(
                key = FormFieldKey.LastName,
                label = "Last Name",
                state = mutableStateOf("")
            ),
            "company" to FormField(
                key = FormFieldKey.Company,
                label = "Company",
                state = mutableStateOf("")
            ),
            "email" to FormField(
                key = FormFieldKey.Email,
                label = "Email",
                state = mutableStateOf("")
            ),
            "password" to FormField(
                key = FormFieldKey.Password,
                label = "Password",
                isSecure = true,
                state = mutableStateOf("")
            ),
            "confirmPassword" to FormField(
                key = FormFieldKey.ConfirmPassword,
                label = "Confirm Password",
                isSecure = true,
                state = mutableStateOf("")
            ),
            "dateOfBirth" to FormField(
                key = FormFieldKey.DateOfBirth,
                label = "Date of Birth",
                state = mutableStateOf("")
            )
        )
    }

    LaunchedEffect(formFields.map { it.value.state.value }) {
        val values = formFields.map { it.value.state.value }
        canSignup = values.all { it.isNotBlank() } && values[4] == values[5]
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
                text = "Sign up",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        }
        formFields.forEach { formField ->
            val field = formField.value
            if (field.key == FormFieldKey.DateOfBirth) {
                DatePicker(
                    onDateSelected = {
                        field.state.value = DateUtils.convertDateFromPatternToISO(it, "d.M.yyyy")
                    },
                    label = field.label,
                )
            } else {
                OutlinedTextField(
                    modifier = fullWidth,
                    value = field.state.value,
                    onValueChange = { field.state.value = it },
                    label = { Text(field.label) },
                    visualTransformation = if (field.isSecure) PasswordVisualTransformation() else VisualTransformation.None,
                    keyboardOptions = if (field.isSecure)
                        KeyboardOptions.Default.copy(
                            autoCorrectEnabled = false,
                            keyboardType = KeyboardType.NumberPassword
                        ) else KeyboardOptions.Default
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                isLoading = true
                showError = false

                CoroutineScope(Dispatchers.IO).launch {
                    val success =
                        ApiClient.signUp(
                            formFields["firstName"]!!.state.value,
                            formFields["lastName"]!!.state.value,
                            formFields["company"]!!.state.value,
                            formFields["email"]!!.state.value,
                            formFields["password"]!!.state.value,
                            formFields["dateOfBirth"]!!.state.value,
                        )
                    withContext(Dispatchers.Main) {
                        isLoading = false
                        if (success) {
                            navController.navigate("home")
                        } else {
                            println("error") // debug
                            showError = true
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            enabled = canSignup && !isLoading
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
