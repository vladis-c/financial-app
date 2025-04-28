package com.vladisc.financial.app.features.notifications

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
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
    val notifications = _userId
        .filterNotNull()
        .flatMapLatest { dao.getNotificationsForUser(it, 999, 0) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), listOf())

    fun setUserId(uid: String) {
        _userId.value = uid
    }

}
