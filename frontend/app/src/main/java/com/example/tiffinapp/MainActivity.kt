package com.example.tiffinapp

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.tiffinapp.core.data.MealRequest
import com.example.tiffinapp.core.domain.AuthRepository
import com.example.tiffinapp.core.presentation.AuthViewModel
import com.example.tiffinapp.core.presentation.LoginScreen
import com.example.tiffinapp.core.presentation.SignupScreen
import com.example.tiffinapp.core.presentation.SplashScreen
import com.example.tiffinapp.core.presentation.UploadScreen
import com.example.tiffinapp.core.util.NavGraph
import com.example.tiffinapp.core.util.TokenManager
import com.example.tiffinapp.home.CartScreen
import com.example.tiffinapp.home.FoodDetailScreen


import com.example.tiffinapp.ui.home.TiffinHomeScreen
import com.example.tiffinapp.ui.home.getSampleMenuItems
import com.example.tiffinapp.ui.theme.TiffinAppTheme
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var tokenManager: TokenManager
    @Inject lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()

            TiffinAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // 🧭 Your full navigation graph
                    NavGraph(
                        navController = navController,
                        tokenManager = tokenManager,
                        authRepository = authRepository
                    )
                  //  UploadScreen()

                }
            }
        }

    }
}




