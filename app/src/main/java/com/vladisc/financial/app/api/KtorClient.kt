package com.vladisc.financial.app.api

import com.vladisc.financial.app.storage.TokenStorage
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object ApiClient {
    const val URL = "http://10.43.142.51:7070"
    val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
        install(io.ktor.client.plugins.DefaultRequest) {
            header("Cookie", buildString {
                TokenStorage.getAccessToken()?.let {
                    append("accessToken=$it; ")
                }
                TokenStorage.getRefreshToken()?.let {
                    append("refreshToken=$it")
                }
            })
        }
    }

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
//        TokenStorage.clear()
        val response = client.post("$URL/auth/validate")
        val setCookieHeaders = response.headers.getAll("Set-Cookie") ?: emptyList()
        TokenStorage.extractCookies(setCookieHeaders)

        return response.status == HttpStatusCode.OK
    }
}