package com.vladisc.financial.app.api

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vladisc.financial.app.api.ApiClient.URL
import com.vladisc.financial.app.api.ApiClient.client
import com.vladisc.financial.app.models.User
import com.vladisc.financial.app.storage.TokenStorage
import io.ktor.client.call.body
import io.ktor.client.request.get
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

object UserApi {
    private var cachedUser: User? = null

    suspend fun get(forceRefresh: Boolean = false): User {
        if (cachedUser != null && !forceRefresh) {
            return cachedUser!!
        }

        val response = client.get("$URL/users/")
        val setCookieHeaders = response.headers.getAll("Set-Cookie") ?: emptyList()
        TokenStorage.extractCookies(setCookieHeaders)
        cachedUser = response.body() as User
        return response.body()
    }

    fun clearCache() {
        cachedUser = null
    }
}