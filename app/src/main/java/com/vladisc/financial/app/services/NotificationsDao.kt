package com.vladisc.financial.app.services

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(notification: Notification)

    @Query("SELECT * FROM notifications WHERE userId = :userId ORDER BY timestamp DESC LIMIT :limit OFFSET :offset")
    suspend fun getNotificationsForUser(userId: String, limit: Int, offset: Int): List<Notification>

    @Query("DELETE FROM notifications WHERE userId = :userId")
    suspend fun deleteNotificationsForUser(userId: String)
}