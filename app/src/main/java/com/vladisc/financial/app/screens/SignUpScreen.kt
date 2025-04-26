package com.vladisc.financial.app.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.vladisc.financial.app.api.AuthApi
import com.vladisc.financial.app.components.DatePicker
import com.vladisc.financial.app.utils.DateUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignUpForm {
    enum class FieldKey {
        FirstName, LastName, Company, Email, Password, ConfirmPassword, DateOfBirth
    }

    data class FormField(
        val key: FieldKey,
        val label: String,
        val isSecure: Boolean = false,
        val state: MutableState<String>
    )
}

@Composable
fun SignUpScreen(navController: NavController) {
    var isLoading by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    var canSignup by remember { mutableStateOf(false) }

    val fullWidth = Modifier
        .fillMaxWidth()
        .height(64.dp)
    val scrollState = rememberScrollState()

    val formFields = remember {
        mapOf(
            SignUpForm.FieldKey.FirstName to SignUpForm.FormField(
                key = SignUpForm.FieldKey.FirstName,
                label = "First Name",
                state = mutableStateOf("")
            ),
            SignUpForm.FieldKey.LastName to SignUpForm.FormField(
                key = SignUpForm.FieldKey.LastName,
                label = "Last Name",
                state = mutableStateOf("")
            ),
            SignUpForm.FieldKey.Company to SignUpForm.FormField(
                key = SignUpForm.FieldKey.Company,
                label = "Company",
                state = mutableStateOf("")
            ),
            SignUpForm.FieldKey.Email to SignUpForm.FormField(
                key = SignUpForm.FieldKey.Email,
                label = "Email",
                state = mutableStateOf("")
            ),
            SignUpForm.FieldKey.Password to SignUpForm.FormField(
                key = SignUpForm.FieldKey.Password,
                label = "Password",
                isSecure = true,
                state = mutableStateOf("")
            ),
            SignUpForm.FieldKey.ConfirmPassword to SignUpForm.FormField(
                key = SignUpForm.FieldKey.ConfirmPassword,
                label = "Confirm Password",
                isSecure = true,
                state = mutableStateOf("")
            ),
            SignUpForm.FieldKey.DateOfBirth to SignUpForm.FormField(
                key = SignUpForm.FieldKey.DateOfBirth,
                label = "Date of Birth",
                state = mutableStateOf("")
            ),
        )
    }

    LaunchedEffect(formFields.map { it.value.state.value }) {
        val values = formFields.map { it.value.state.value }
        canSignup = values.all { it.isNotBlank() } && values[4] == values[5]
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
            if (field.key == SignUpForm.FieldKey.DateOfBirth) {
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
                        ) else KeyboardOptions.Default.copy(
                        capitalization = if (field.key == SignUpForm.FieldKey.FirstName || field.key == SignUpForm.FieldKey.LastName || field.key == SignUpForm.FieldKey.Company)
                            KeyboardCapitalization.Words else KeyboardCapitalization.None
                    )
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
                        AuthApi.signUp(
                            formFields[SignUpForm.FieldKey.FirstName]!!.state.value,
                            formFields[SignUpForm.FieldKey.LastName]!!.state.value,
                            formFields[SignUpForm.FieldKey.Company]!!.state.value,
                            formFields[SignUpForm.FieldKey.Email]!!.state.value,
                            formFields[SignUpForm.FieldKey.Password]!!.state.value,
                            formFields[SignUpForm.FieldKey.DateOfBirth]!!.state.value,
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

        TextButton(
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .fillMaxWidth()
                .height(48.dp),
            onClick = { navController.navigate("login") }) {
            Text(
                text = "Already have an account? Log in",
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}
