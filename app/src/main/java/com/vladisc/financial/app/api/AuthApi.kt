package com.vladisc.financial.app.api

import com.vladisc.financial.app.api.ApiClient.URL
import com.vladisc.financial.app.api.ApiClient.client
import com.vladisc.financial.app.storage.TokenStorage
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

object AuthApi {
    suspend fun signUp(
        firstName: String,
        lastName: String,
        company: String,
        email: String,
        password: String,
        dateOfBirth: String
    ): Boolean {
        val response = client.post("$URL/auth/signup") {
            contentType(ContentType.Application.Json)
            setBody(
                mapOf(
                    "firstName" to firstName,
                    "lastName" to lastName,
                    "company" to company,
                    "email" to email,
                    "password" to password,
                    "dateOfBirth" to dateOfBirth
                )
            )
        }
        val setCookieHeaders = response.headers.getAll("Set-Cookie") ?: emptyList()
        TokenStorage.extractCookies(setCookieHeaders)
        return response.status == HttpStatusCode.Created
    }

    suspend fun login(
        email: String,
        password: String,
    ): Boolean {
        val response = client.post("$URL/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(
                mapOf(
                    "email" to email,
                    "password" to password,
                )
            )
        }
        val setCookieHeaders = response.headers.getAll("Set-Cookie") ?: emptyList()
        TokenStorage.extractCookies(setCookieHeaders)

        return response.status == HttpStatusCode.OK
    }

    suspend fun validate(): Boolean {
        TokenStorage.clear()
        val response = client.post("$URL/auth/validate")
        val setCookieHeaders = response.headers.getAll("Set-Cookie") ?: emptyList()
        TokenStorage.extractCookies(setCookieHeaders)

        return response.status == HttpStatusCode.OK
    }
}