package com.vladisc.financial.app.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.vladisc.financial.app.utils.DateUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePicker(
    label: String = "Date of Birth",
    onDateSelected: (String) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    var lastSelectedDate by remember { mutableStateOf<Long?>(null) }

    val selectedDate = datePickerState.selectedDateMillis?.let {
        DateUtils.convertMillisToDate(it)
    } ?: ""

    LaunchedEffect(datePickerState.selectedDateMillis) {
        val newDate = datePickerState.selectedDateMillis
        if (newDate != null && newDate != lastSelectedDate) {
            lastSelectedDate = newDate
            onDateSelected(selectedDate)
            showDatePicker = false
        }
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
        ) {
            OutlinedTextField(
                value = selectedDate,
                onValueChange = {},
                label = { Text(label) },
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Select date"
                        )
                    }
                },
                modifier = Modifier
                    .matchParentSize(),
                enabled = true,
                readOnly = true,
            )
        }

        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable { showDatePicker = true }
        )

        if (showDatePicker) {
            Dialog(
                onDismissRequest = { showDatePicker = false },
                properties = DialogProperties(
                    dismissOnClickOutside = true,
                    usePlatformDefaultWidth = false
                )
            ) {
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    tonalElevation = 4.dp,
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) {
                    DatePicker(
                        state = datePickerState,
                        showModeToggle = true,

                    )
                }
            }
        }
    }
}
