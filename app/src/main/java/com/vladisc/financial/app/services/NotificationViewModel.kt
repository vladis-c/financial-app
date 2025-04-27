package com.vladisc.financial.app.services

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NotificationViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        fun provideFactory(application: Application): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return NotificationViewModel(application) as T
                }
            }
        }
    }

    private val dao = AppDatabase.getInstance(application).notificationDao()

    private val _userId = MutableStateFlow<String?>(null)
    private val _notifications = MutableStateFlow<List<Notification>>(emptyList())
    val notifications: StateFlow<List<Notification>> = _notifications

    private var currentOffset = 0
    private val pageSize = 20

    fun setUserId(uid: String) {
        _userId.value = uid
        currentOffset = 0
        _notifications.value = emptyList()
        loadMore()
    }

    fun loadMore() {
        val uid = _userId.value ?: return

        viewModelScope.launch {
            val newItems = dao.getNotificationsForUser(uid, pageSize, currentOffset)
            _notifications.value = _notifications.value + newItems
            currentOffset += newItems.size
        }
    }
}
