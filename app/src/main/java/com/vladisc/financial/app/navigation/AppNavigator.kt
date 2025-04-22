package com.vladisc.financial.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vladisc.financial.app.screens.LoginScreen
import com.vladisc.financial.app.screens.SignUpScreen

@Composable
fun AppNavigator() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "signup") {
        composable("signup") { SignUpScreen(navController) }
        composable("login") { LoginScreen(navController) }
//        composable("home/{firstName}/{lastName}") { backStackEntry ->
//            val firstName = backStackEntry.arguments?.getString("firstName") ?: ""
//            val lastName = backStackEntry.arguments?.getString("lastName") ?: ""
//            HomeScreen(firstName, lastName)
//        }
    }
}
