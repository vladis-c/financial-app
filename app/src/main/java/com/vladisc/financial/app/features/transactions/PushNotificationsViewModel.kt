package com.vladisc.financial.app.features.transactions

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class PushNotificationsViewModel : ViewModel() {
    private val _pushNotification = mutableStateOf<PushNotification?>(null)
    val pushNotification: State<PushNotification?> = _pushNotification

    fun getNotification(forceRefresh: Boolean = false, transactionId: String) {
        viewModelScope.launch {
            try {
                _pushNotification.value = PushNotificationsApi.get(
                    forceRefresh,
                    transactionId
                )
            } catch (e: Exception) {
                println("Failed to fetch notification: ${e.message}")
            }
        }
    }
}