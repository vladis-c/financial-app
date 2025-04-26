package com.vladisc.financial.app.services

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class Notification(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: String,
    val packageName: String,
    val title: String?,
    val body: String?,
    val timestamp: Long
)