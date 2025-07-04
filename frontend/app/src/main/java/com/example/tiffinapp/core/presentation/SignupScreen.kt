@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.tiffinapp.core.presentation

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.tiffinapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(navController: NavController, viewModel: AuthViewModel = hiltViewModel()) {
    var fullName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var AgreedTerms by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    val uiState by viewModel.uiState.collectAsState()
    val colors = MaterialTheme.colorScheme

    Box(modifier = Modifier.fillMaxSize().background(colors.background)) {
        BackgroundPattern()

        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Tiffin", fontSize = 48.sp, fontWeight = FontWeight.Bold, color = colors.primary)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Create new account", fontSize = 18.sp, color = colors.onBackground)
            Spacer(modifier = Modifier.height(16.dp))

            LabelText("Full Name")
            ThemedTextField(fullName, "Enter your name") { fullName = it }

            Spacer(modifier = Modifier.height(8.dp))
            LabelText("Email")
            ThemedTextField(email, "Enter your email") { email = it }

            Spacer(modifier = Modifier.height(8.dp))
            LabelText("Password")
            ThemedPasswordField(password, passwordVisible, { password = it }) { passwordVisible = !passwordVisible }

            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                RadioButton(
                    selected = AgreedTerms,
                    onClick = { AgreedTerms = !AgreedTerms },
                    colors = RadioButtonDefaults.colors(selectedColor = colors.primary)
                )
                Text("I've read and agreed to Terms of Use and Privacy Policy.", color = colors.outline)
            }

            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    viewModel.registerUser(fullName, email, password)
                },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colors.primary),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Register Account", color = colors.onPrimary, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text("or sign up with", fontSize = 14.sp, color = colors.outline)
            Spacer(modifier = Modifier.height(8.dp))
            SocialLoginRow()

            Spacer(modifier = Modifier.height(4.dp))
            TextButton(onClick = {}) {
                Text("Already have an account? ", color = colors.outline)
                Text("Back to Sign In ", color = colors.primary)
            }
        }

        when (uiState) {
            is RegisterUiState.Loading -> LoadingState("Loading...")
            is RegisterUiState.Success -> MessageState("Welcome, ${(uiState as RegisterUiState.Success).message}!", colors.primary)
            is RegisterUiState.Error -> MessageState((uiState as RegisterUiState.Error).message, Color.Red)
            else -> {}
        }
    }
}



@Composable
fun BackgroundPattern() {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(screenHeight * 0.5f) // Adjust height to fit background
    ) {

    }
}

