package com.vladisc.financial.app.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val uid: String? = null,
    val email: String? = null,
    val password: String? = null,
    val newPassword: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val dateOfBirth: String? = null,
    val company: String? = null,
)
