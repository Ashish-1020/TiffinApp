package com.example.tiffinapp.core.util

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tiffinapp.core.domain.AuthRepository
import com.example.tiffinapp.core.presentation.LoginScreen
import com.example.tiffinapp.core.presentation.SignupScreen
import com.example.tiffinapp.core.presentation.SplashScreen
import com.example.tiffinapp.core.presentation.UploadScreen
import com.example.tiffinapp.home.FoodDetailScreen
import com.example.tiffinapp.home.MainScreen
import com.example.tiffinapp.home.TiffinHomeScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavGraph(
    navController: NavHostController,
    tokenManager: TokenManager,
    authRepository: AuthRepository
) {
    // Always start from splash screen
    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(
                navController = navController,
                tokenManager = tokenManager,
                authRepository = authRepository
            )
        }
        composable("login") { LoginScreen(navController) }
        composable("MainScreen") { MainScreen(navController) }
        composable("register") { SignupScreen(navController) }
        composable("upload"){UploadScreen()}
        composable("home"){TiffinHomeScreen(navController)}
        composable("fooddetail/{mealId}") { backStackEntry ->
            val mealId = backStackEntry.arguments?.getString("mealId") ?: ""
            FoodDetailScreen(mealId,navController)
        }

    }
}


