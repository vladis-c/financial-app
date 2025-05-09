package com.vladisc.financial.app

import com.vladisc.financial.app.features.auth.TokenStorage
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import java.util.concurrent.TimeUnit

object ApiClient {
//    const val URL = "http://10.43.142.51:7070"

        const val URL = "http://192.168.1.203:7070"
    val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = false
                isLenient = true
            })
        }
        engine {
            config {
                connectTimeout(30, TimeUnit.SECONDS)
                readTimeout(30, TimeUnit.SECONDS)
                writeTimeout(30, TimeUnit.SECONDS)
            }
        }
        install(DefaultRequest) {
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
}