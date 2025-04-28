package com.vladisc.financial.app

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.vladisc.financial.app.features.auth.TokenStorage
import com.vladisc.financial.app.features.notifications.NotificationListener
import com.vladisc.financial.app.features.user.UserViewModel
import com.vladisc.financial.app.ui.theme.FinancialAppTheme

class MainActivity : ComponentActivity() {
    val userViewModel = UserViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TokenStorage.init(this)
        enableEdgeToEdge()
        setContent {
            FinancialAppTheme {
                AppNavigator(userViewModel)
            }
        }
        // enabling push notifications interception
        if (!isNotificationServiceEnabled()) {
            val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
            startActivity(intent)
        }
    }

    private fun isNotificationServiceEnabled(): Boolean {
        val cn = ComponentName(this, NotificationListener::class.java)
        val flat = Settings.Secure.getString(contentResolver, "enabled_notification_listeners")
        return flat != null && flat.contains(cn.flattenToString())
    }
}
