package com.vladisc.financial.app.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
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
        return response.status == HttpStatusCode.Created
    }
}