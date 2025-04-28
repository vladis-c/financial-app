package com.vladisc.financial.app.features.user

import com.vladisc.financial.app.ApiClient
import com.vladisc.financial.app.features.auth.TokenStorage
import io.ktor.client.call.body
import io.ktor.client.request.get

object UserApi {
    private var cachedUser: User? = null

    suspend fun get(forceRefresh: Boolean = false): User {
        if (cachedUser != null && !forceRefresh) {
            return cachedUser!!
        }

        val response = ApiClient.client.get("${ApiClient.URL}/users/")
        val setCookieHeaders = response.headers.getAll("Set-Cookie") ?: emptyList()
        TokenStorage.extractCookies(setCookieHeaders)
        cachedUser = response.body() as User
        return response.body()
    }

    fun clearCache() {
        cachedUser = null
    }
}