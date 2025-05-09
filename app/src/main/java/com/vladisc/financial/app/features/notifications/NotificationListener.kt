package com.vladisc.financial.app.features.notifications

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

// TODO check WorkManager background service
class NotificationListener() : NotificationListenerService() {
    private lateinit var db: AppDatabase
    override fun onCreate() {
        super.onCreate()
        db = AppDatabase.getInstance(applicationContext)
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
        Log.d("NotificationListener", "Listener connected")
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
        Log.d("NotificationListener", "Notification removed: ${sbn?.packageName}")
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val prefs = applicationContext.getSharedPreferences("prefs", MODE_PRIVATE)
        val userId = prefs.getString("user_id", null)

        if (userId.isNullOrBlank()) {
            println("No user id found in SharedPreferences")
            return
        }

        val packageName = sbn.packageName
        val notification = sbn.notification

        val title = notification.extras.getString(android.app.Notification.EXTRA_TITLE)
        var text = notification.extras.getString(android.app.Notification.EXTRA_TEXT)
        val bigText = notification.extras.getString(android.app.Notification.EXTRA_BIG_TEXT)
        val subText = notification.extras.getString(android.app.Notification.EXTRA_SUB_TEXT)
        val infoText = notification.extras.getString(android.app.Notification.EXTRA_INFO_TEXT)

        // fallback to bigText, subText, infoText if needed
        if (text.isNullOrBlank()) {
            text = bigText ?: subText ?: infoText
        }

        Log.d("NotificationListener", "Notification from $packageName: $title - $text - $bigText - $subText - $infoText")


        if (shouldMonitorPackage(packageName)) {
            val interceptedNotification = Notification(
                userId = userId,
                packageName = packageName,
                title = title,
                body = text,
                timestamp = System.currentTimeMillis(),
            )

            GlobalScope.launch {
                println("Inserting $interceptedNotification")
                if (interceptedNotification.title.isNullOrBlank()) {
                    return@launch
                }
                db.notificationDao().insert(interceptedNotification)
            }
        }
    }

    private fun shouldMonitorPackage(packageName: String): Boolean {
        val prefs = applicationContext.getSharedPreferences("prefs", 0)
        val allowedApps = prefs.getStringSet("allowed_apps", emptySet()) ?: emptySet()

        println("Checking $packageName against allowed apps: $allowedApps")

        return allowedApps.contains(packageName)
    }
}