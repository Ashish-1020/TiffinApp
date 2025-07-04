package com.example.tiffinapp.home

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size

import androidx.compose.material.icons.Icons


import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.material3.*
import androidx.compose.ui.text.style.TextDecoration
import androidx.navigation.NavController

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.DirectionsBike

import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.tiffinapp.R
import com.google.accompanist.pager.*
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf("home") }
    val context = LocalContext.current

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            BottomNavigationBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedTab) {
                "home" -> TiffinHomeScreen(navController)
                "cart" -> CartScreen()
                "wallet" -> WalletScreen()
                "track" -> OrderTrackingScreen()
            }
        }
    }
}

@Composable
fun BottomNavigationBar(selectedTab: String, onTabSelected: (String) -> Unit) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.primary
    ) {
        val selectedColor = MaterialTheme.colorScheme.primary
        val unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant

        val navItems = listOf(
            Triple(Icons.Outlined.Home, "Home", "home"),
            Triple(Icons.Outlined.ShoppingCart, "Cart", "cart"),
            Triple(Icons.Outlined.AccountBalanceWallet, "Wallet", "wallet"),
            Triple(null, "Track", "track") // special handling for image icon
        )

        navItems.forEach { (icon, label, route) ->
            val isSelected = selectedTab == route

            NavigationBarItem(
                icon = {
                    if (label == "Track") {
                        // Use custom drawables for Track
                        val imageRes = if (isSelected)
                            R.drawable.delivery_orange
                        else
                            R.drawable.delivery_black

                        Icon(
                            painter = painterResource(id = imageRes),
                            contentDescription = label,
                            modifier = Modifier.size(28.dp),
                            tint = Color.Unspecified // Use original image color
                        )
                    } else {
                        Icon(
                            imageVector = icon!!,
                            contentDescription = label
                        )
                    }
                },
                label = { Text(label) },
                selected = isSelected,
                onClick = { onTabSelected(route) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = selectedColor,
                    unselectedIconColor = unselectedColor,
                    selectedTextColor = selectedColor,
                    unselectedTextColor = unselectedColor,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}
