package com.vladisc.financial.app.features.user

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
    private val _user = mutableStateOf<User?>(null)
    val user: State<User?> = _user

    fun getUser(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            try {
                _user.value = UserApi.get(forceRefresh)
            } catch (e: Exception) {
                println("Failed to fetch user: ${e.message}")
            }
        }
    }

    fun clearUser() {
        _user.value = null
        UserApi.clearCache()
    }
}
