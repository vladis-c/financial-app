package com.vladisc.financial.app.features.transactions

import com.vladisc.financial.app.ApiClient
import com.vladisc.financial.app.features.auth.TokenStorage
import io.ktor.client.call.body
import io.ktor.client.request.get

object TransactionsApi {
    private var cachedTransactions: List<Transaction>? = null

    suspend fun get(
        forceRefresh: Boolean = false,
        startDate: String? = null,
        endDate: String? = null
    ): List<Transaction> {
        if (cachedTransactions != null && !forceRefresh) {
            return cachedTransactions!!
        }

        val response = ApiClient.client.get("${ApiClient.URL}/users/transactions/") {
            url {
                startDate?.let { parameters.append("start_date", it) }
                endDate?.let { parameters.append("end_date", it) }
            }
        }
        val setCookieHeaders = response.headers.getAll("Set-Cookie") ?: emptyList()
        TokenStorage.extractCookies(setCookieHeaders)
        cachedTransactions = response.body() as List<Transaction>
        return response.body()
    }

    fun clearCache() {
        cachedTransactions = null
    }
}