package com.vladisc.financial.app

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vladisc.financial.app.features.auth.LoginScreen
import com.vladisc.financial.app.features.auth.SignUpScreen
import com.vladisc.financial.app.features.auth.SplashScreen
import com.vladisc.financial.app.features.home.HomeScreen
import com.vladisc.financial.app.features.selectapps.SelectAppsScreen
import com.vladisc.financial.app.features.user.UserViewModel

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
