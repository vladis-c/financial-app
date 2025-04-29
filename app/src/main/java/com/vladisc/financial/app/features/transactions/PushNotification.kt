package com.vladisc.financial.app.features.transactions

import kotlinx.serialization.Serializable

@Serializable
data class PushNotification(
    val id: String? = null,
    val timestamp: String? = null,
    val title: String? = null,
    val body: String? = null,
    val transactionId: String? = null,
)
