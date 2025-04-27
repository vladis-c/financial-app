package com.vladisc.financial.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vladisc.financial.app.api.UserViewModel
import com.vladisc.financial.app.screens.HomeScreen
import com.vladisc.financial.app.screens.LoginScreen
import com.vladisc.financial.app.screens.SelectAppsScreen
import com.vladisc.financial.app.screens.SignUpScreen
import com.vladisc.financial.app.screens.SplashScreen

@Composable
fun AppNavigator(userViewModel: UserViewModel){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController) }
        composable("signup") { SignUpScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("home") { HomeScreen(navController, userViewModel) }
        composable("select_apps") { SelectAppsScreen(navController) }
    }
}
