package com.vladisc.financial.app.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object TokenStorage {
    private const val PREF_NAME = "auth_prefs"
    private const val KEY_ACCESS_TOKEN = "accessToken"
    private const val KEY_REFRESH_TOKEN = "refreshToken"

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveCookies(accessToken: String, refreshToken: String) {
        prefs.edit() {
            putString(KEY_ACCESS_TOKEN, accessToken)
                .putString(KEY_REFRESH_TOKEN, refreshToken)
        }
    }

    fun getAccessToken(): String? = prefs.getString(KEY_ACCESS_TOKEN, null)
    fun getRefreshToken(): String? = prefs.getString(KEY_REFRESH_TOKEN, null)

    fun clear() {
        prefs.edit() { clear() }
    }

    fun extractCookies(setCookieHeaders: List<String>) {
        var accessToken: String? = null
        var refreshToken: String? = null

        for (cookie in setCookieHeaders) {
            if (cookie.startsWith("accessToken")) {
                accessToken = cookie.substringAfter("accessToken=").substringBefore(";")
            } else if (cookie.startsWith("refreshToken")) {
                refreshToken = cookie.substringAfter("refreshToken=").substringBefore(";")
            }
        }

        if (accessToken != null && refreshToken != null) {
            saveCookies(accessToken, refreshToken)
        }
    }
}
