package com.vladisc.financial.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.vladisc.financial.app.ui.theme.FinancialAppTheme
import com.vladisc.financial.app.navigation.AppNavigator
import com.vladisc.financial.app.storage.TokenStorage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TokenStorage.init(this)
        enableEdgeToEdge()
        setContent {
            FinancialAppTheme {
                AppNavigator()
            }
        }
    }
}
