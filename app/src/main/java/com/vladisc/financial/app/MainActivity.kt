package com.vladisc.financial.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.vladisc.financial.app.ui.theme.FinancialAppTheme
import com.vladisc.financial.app.navigation.AppNavigator

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FinancialAppTheme {
                AppNavigator()
            }
        }
    }
}
