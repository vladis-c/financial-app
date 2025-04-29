package com.vladisc.financial.app.features.transactions

import com.vladisc.financial.app.ApiClient
import com.vladisc.financial.app.features.auth.TokenStorage
import io.ktor.client.call.body
import io.ktor.client.request.get

object PushNotificationsApi {
    private var cachedPushNotification: PushNotification? = null

    suspend fun get(forceRefresh: Boolean = false, transactionId: String): PushNotification {
        if(cachedPushNotification != null && !forceRefresh) {
            return cachedPushNotification!!
        }

        val response = ApiClient.client.get("${ApiClient.URL}/users/notifications/${transactionId}")
        val setCookieHeaders = response.headers.getAll("Set-Cookie") ?: emptyList()
        TokenStorage.extractCookies(setCookieHeaders)
        cachedPushNotification = response.body() as PushNotification
        return response.body()
    }
}