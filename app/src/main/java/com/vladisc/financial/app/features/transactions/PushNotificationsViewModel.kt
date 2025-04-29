package com.vladisc.financial.app.features.transactions

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PushNotificationsViewModel : ViewModel() {
    private val _pushNotification = mutableStateOf<PushNotification?>(null)
    val pushNotification: State<PushNotification?> = _pushNotification

    fun getPushNotification(forceRefresh: Boolean = false, transactionId: String) {
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

    fun postPushNotificationsInBatches(
        pushNotifications: List<PushNotification>,
        onComplete: () -> Unit,
        onBatchComplete: (ids: List<Int?>) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val batchSize = 10
                val total = pushNotifications.size
                var index = 0

                while (index < total) {
                    val batch = pushNotifications.subList(index, minOf(index + batchSize, total))
                    try {
                        PushNotificationsApi.post(batch)
                    } catch (e: Exception) {
                        e.printStackTrace() // Shows full stacktrace
                        println("Failed to upload notifications: ${e.localizedMessage}")
                    }
                    index += batchSize
                    delay(500) // short delay between batches (adjust as needed)
                    onBatchComplete(pushNotifications.map { it.id?.toInt() })
                }

                onComplete()
            } catch (e: Exception) {
                println("Failed to upload notifications: ${e.message}")
                onComplete() // still stop spinner on failure
            }
        }
    }
}