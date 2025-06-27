package com.example.tiffinapp.core.presentation
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
import androidx.compose.runtime.LaunchedEffect
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
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController,viewModel: AuthViewModel = hiltViewModel()) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val uiState by viewModel.loginuiState.collectAsState()
    val colors = MaterialTheme.colorScheme

    LaunchedEffect(uiState) {
        if (uiState is LoginUiState.Success) {
         //   delay(500) // Optional delay for showing message
            navController.navigate("home") {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colors.background)
    ) {
        Background()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Tiffin", fontSize = 48.sp, fontWeight = FontWeight.Bold, color = colors.primary)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Welcome back", fontSize = 18.sp, color = colors.onBackground)
            Spacer(modifier = Modifier.height(24.dp))

            LabelText("Email")
            ThemedTextField(email, "Enter your email") { email = it }

            Spacer(modifier = Modifier.height(8.dp))
            LabelText("Password")
            ThemedPasswordField(password, passwordVisible, { password = it }) { passwordVisible = !passwordVisible }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { viewModel.loginUser(email, password) },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colors.primary),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Login", color = colors.onPrimary, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text("or continue with", fontSize = 14.sp, color = colors.outline)
            Spacer(modifier = Modifier.height(8.dp))

            SocialLoginRow()

            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = {}) {
                Text("Don't have an account? ", color = colors.outline)
                Text("Create one", color = colors.primary)
            }
        }

        when (uiState) {
            is LoginUiState.Loading -> LoadingState("Logging in...")
           // is LoginUiState.Success -> MessageState((uiState as LoginUiState.Success).message, colors.primary)
            is LoginUiState.Error -> MessageState((uiState as LoginUiState.Error).message, Color.Red)
            else -> {}
        }
    }
}
@Composable
fun LabelText(label: String) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Start,
            modifier = Modifier.align(Alignment.CenterStart)
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemedTextField(value: String, placeholderText: String, onValueChange: (String) -> Unit) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholderText, color = MaterialTheme.colorScheme.outline) },
        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp).border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp)),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = MaterialTheme.colorScheme.surface,
            cursorColor = MaterialTheme.colorScheme.onSurface,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface),
        singleLine = true
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemedPasswordField(
    value: String,
    visible: Boolean,
    onValueChange: (String) -> Unit,
    toggleVisibility: () -> Unit
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text("Enter your password", color = MaterialTheme.colorScheme.outline) },
        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp).border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp)),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = MaterialTheme.colorScheme.surface,
            cursorColor = MaterialTheme.colorScheme.onSurface,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface),
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = toggleVisibility) {
                Icon(
                    painter = painterResource(id = if (visible) R.drawable.visible_eye_svgrepo_com else R.drawable.invisible_svgrepo_com),
                    contentDescription = if (visible) "Hide password" else "Show password",
                    tint = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        singleLine = true
    )
}

@Composable
fun SocialLoginRow() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        listOf(R.drawable.google_color_svgrepo_com, R.drawable.facebook_svgrepo_com__1_).forEach { iconId ->
            Box(
                modifier = Modifier.size(48.dp).border(1.dp, MaterialTheme.colorScheme.outline, CircleShape).padding(12.dp).clip(CircleShape)
            ) {
                Image(
                    painter = painterResource(id = iconId),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

@Composable
fun LoadingState(message: String) {
    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xAA000000)).padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(color = Color.White)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = message, color = Color.White)
    }
}

@Composable
fun MessageState(message: String, color: Color) {
    Box(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = message, color = color, fontSize = 18.sp)
    }
}

@Composable
fun Background() {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(screenHeight * 0.5f)
    ) {}
}

/*
@Composable
@Preview(showBackground = true)
fun previewLoginscreen(){
    LoginScreen()
}
*/