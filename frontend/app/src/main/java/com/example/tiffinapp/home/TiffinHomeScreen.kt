package com.example.tiffinapp.home

import android.Manifest
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WatchLater
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiffinapp.R
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.withStyle
import androidx.compose.material3.*
import androidx.compose.ui.text.style.TextDecoration
import androidx.navigation.NavController

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.KeyboardArrowDown

import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow

import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.tiffinapp.cart.CartViewModel
import com.example.tiffinapp.core.data.MealResponse
import com.example.tiffinapp.core.util.LocationUtils
import com.google.accompanist.pager.*




import dev.chrisbanes.snapper.ExperimentalSnapperApi
@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalPagerApi::class,
    ExperimentalSnapperApi::class
)
@Composable
fun TiffinHomeScreen(navController: NavController) {
    val context = LocalContext.current

    var userAddress by remember { mutableStateOf("Fetching location...") }


    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // ✅ Fetch location and store it
            LocationUtils.fetchAndStoreLocation(context) { success ->
                if (success) {
                    userAddress = LocationUtils.getAddress(context) ?: "Address not found"
                } else {
                    userAddress = "Location not available"
                }
            }
        } else {
            userAddress = "Permission denied"
        }
    }



    val viewModel: HomeViewModel = hiltViewModel()
    val cartViewModel: CartViewModel = hiltViewModel()

    val mealList by viewModel.mealList.collectAsState()
    val cartItems by cartViewModel.cartItems.observeAsState(emptyList())
    val cartItemIds = remember(cartItems) { cartItems.map { it.mealId }.toSet() }

    val favoriteItems = remember { mutableStateListOf<String>() }
    var selectedCategory by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }

    var filteredMeals = mealList.filter {
        (selectedCategory == "All" || it.name.contains(selectedCategory, ignoreCase = true)) &&
                it.name.contains(searchQuery, ignoreCase = true)
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        viewModel.getAllMeal()
        cartViewModel.fetchCart() // 👈 Fetch cart once on screen load
    }

    val pagerState = rememberPagerState(initialPage = 0)

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 16.dp)
            ) {
                // Location + Profile Icon Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Location",
                            tint = Color.Red
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Column {
                        Text(
                            text = "Location",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                val shortAddress = userAddress
                                    .split(",")
                                    .take(3)
                                    .joinToString(",")
                                    .take(25)

                                Text(
                                    text = shortAddress,
                                    fontSize = 14.sp,
                                    color = Color.Gray,
                                    maxLines = 1
                                )


                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = "Profile",
                                    tint = Color(0xFFFF9800) // Orange
                                )
                            }
                        }
                    }

                    IconButton(onClick = { /* Navigate to Profile */ }) {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = "Profile",
                            tint = Color(0xFFFF9800) // Orange
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Search Bar with functionality
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clip(RoundedCornerShape(50))
                        .background(Color.Gray.copy(0.2f)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(12.dp))
                    Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray)
                    Spacer(modifier = Modifier.width(8.dp))
                    TextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = {
                            Text("Search tiffin™", color = Color.Gray)
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            //textColor = Color.Black
                        ),
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .background(Color.Transparent)
                    )
                    Icon(Icons.Default.Mic, contentDescription = null, tint = Color.Red)
                    Spacer(modifier = Modifier.width(12.dp))
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
        },
        containerColor = Color.White
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(top = 84.dp, start = 16.dp, end = 16.dp)
                .fillMaxSize()
                .background(Color.White)
        ) {
            item {
              //  HeaderSection()
                Spacer(modifier = Modifier.height(24.dp))
                OfferCarousel()
                Spacer(modifier = Modifier.height(12.dp))

                FoodCategorySection { category ->
                    selectedCategory = category
                    Log.d("SelectedCategory", category)
                }

                Spacer(modifier = Modifier.height(24.dp))
                Text("Recommended Meals", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Spacer(modifier = Modifier.height(12.dp))
            }

            item {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(4.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.heightIn(max = 1200.dp)
                ) {
                    items(filteredMeals) { meal ->
                        FoodItemCard(
                            viewModel = viewModel,
                            meal = meal,
                            isInCart = cartItemIds.contains(meal.id), // ✅ From LiveData
                            isFavorite = favoriteItems.contains(meal.id),
                            onAddToCart = {
                                cartViewModel.addItem(meal.id, 1) // ✅ Call ViewModel function
                            },
                            onFavorite = {
                                if (favoriteItems.contains(meal.id)) {
                                    favoriteItems.remove(meal.id)
                                } else {
                                    favoriteItems.add(meal.id)
                                }
                            },
                            navController = navController
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}



@OptIn(ExperimentalPagerApi::class, ExperimentalSnapperApi::class)
@Composable
fun OfferCarousel() {
    val offers = listOf(
        Offer(
            title = "Weekend Special 🍕",
            description = "Use code WEEKEND20 for 20% OFF",
            color = Color(0xFFFF5722) // Orange
        ),
        Offer(
            title = "Healthy Choice 🥗",
            description = "Save ₹100 with code HEALTH100",
            color = Color(0xFF4CAF50) // Green
        ),
        Offer(
            title = "First Order Bonus 🎉",
            description = "Flat 30% OFF • Code: FIRST30",
            color = Color(0xFF3F51B5) // Indigo
        )
    )

    val pagerCount = offers.size
    val pagerState = rememberPagerState(initialPage = 0)

    val coroutineScope = rememberCoroutineScope()
    val progress = remember { Animatable(0f) }

    // Auto-scroll every 3 seconds
    LaunchedEffect(pagerState) {
        while (true) {
           // delay(1000L)
            progress.snapTo(0f)
            progress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 8000, easing = LinearEasing)
            )
            val nextPage = (pagerState.currentPage + 1) % pagerCount

            pagerState.animateScrollToPage(nextPage)
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalPager(
            count = pagerCount,
            state = pagerState,
            reverseLayout = false,
            itemSpacing = 24.dp,
            contentPadding = PaddingValues(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            flingBehavior = PagerDefaults.flingBehavior(state = pagerState),
            key = { index -> "OfferCard-$index" },
            userScrollEnabled = false,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) { page ->
            val offer = offers[page]
            OfferCard(
                title = offer.title,
                description = offer.description,
                color = offer.color,
                progress = progress.value
            )
        }




    }
}



@Composable
fun FoodCategorySection(
    onCategorySelected: (String) -> Unit
) {
    val categories = listOf("Burger", "Pizza", "Fries", "Drinks", "Meat")
    val icons = listOf(
        R.drawable.ic_sample_item, R.drawable.pizza, R.drawable.fries,
        R.drawable.drinks, R.drawable.meat
    )

    var selectedIndex by remember { mutableStateOf(-1) }

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(categories.size) { index ->
            val isSelected = index == selectedIndex

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .pointerInput(Unit) {} // To prevent ripple
                    .clickable(
                        indication = null, // No ripple
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        if (isSelected) {
                            // Deselect if same item clicked
                            selectedIndex = -1
                            onCategorySelected("All")
                        } else {
                            selectedIndex = index
                            onCategorySelected(categories[index])
                        }
                    }
            ) {
                Card(
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) Color(0xFFFF9800) else Color(0xFFF5F5F5),
                        contentColor = Color.Black
                    ),
                    modifier = Modifier.size(60.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Image(
                        painter = painterResource(id = icons[index]),
                        contentDescription = categories[index],
                        modifier = Modifier.padding(16.dp)
                    )
                }
                Text(text = categories[index], fontSize = 12.sp)
            }
        }
    }
}




@Composable
fun OfferCard(
    title: String,
    description: String,
    color: Color,
    progress: Float
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(20.dp))
    ) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.back_crousel),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        // Optional color overlay for contrast
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(color.copy(alpha = 0.5f))
        )

        // Progress bar overlay near top or bottom
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .align(Alignment.BottomCenter)
                .padding(horizontal = 32.dp, vertical = 8.dp),
            color = Color.White,
            trackColor = Color.Gray.copy(alpha = 0.3f)
        )

        // Content overlay
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "$title\n$description",
                    color = Color.White,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Hurry, offer ends soon!",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Button(
                onClick = { /* TODO */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                shape = RoundedCornerShape(50)
            ) {
                Text("Order Now", color = Color.White)
            }
        }
    }
}


@Composable
fun FoodItemCard(
    viewModel: HomeViewModel,
    meal: MealResponse,
    isInCart: Boolean,
    isFavorite: Boolean,
    onAddToCart: (MealResponse) -> Unit,
    onFavorite: (MealResponse) -> Unit,
    navController: NavController
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable {   navController.navigate("fooddetail/${meal.id}") }
    ) {
        Column {
            Box(modifier = Modifier.height(120.dp)) {
                AsyncImage(
                    model = meal.imgurl,
                    contentDescription = meal.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth()
                )

                // ETA placeholder (could use fixed/dynamic)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                        .background(Color.White, RoundedCornerShape(12.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(Icons.Default.WatchLater, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("25 min", fontSize = 12.sp, color = Color.Gray)
                }

                IconButton(
                    onClick = { onFavorite(meal) },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .background(Color.White.copy(0.9f), CircleShape)
                        .size(28.dp)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = Color.Red
                    )
                }

            }

            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    meal.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )


                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(meal.rating.toString(), fontSize = 13.sp)
                        Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFC107), modifier = Modifier.size(16.dp))
                      //  Text("(${meal.noofreviews})", fontSize = 13.sp, color = Color.Gray)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocalFireDepartment, contentDescription = "Calories", tint = Color(0xFFFF5722), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("${meal.calorie}", fontSize = 13.sp)
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "₹${(meal.price * (100 - meal.offer)) / 100}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        if (meal.offer != 0) {
                            Text(
                                text = "₹${meal.price}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    textDecoration = TextDecoration.LineThrough,
                                    color = Color.Gray
                                )
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (meal.offer != 0) {
                            Surface(color = Color(0xFFD32F2F), shape = RoundedCornerShape(4.dp)) {
                                Text(
                                    "${meal.offer}% OFF",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                        }

                        IconButton(
                            onClick = { onAddToCart(meal) },
                            modifier = Modifier
                                .background(
                                    color = if (isInCart) Color.Black else Color(0xFFFF5722),
                                    shape = CircleShape
                                )
                                .size(28.dp)
                        ) {
                            Icon(
                                imageVector = if (isInCart) Icons.Default.Check else Icons.Default.Add,
                                contentDescription = "Add",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                    }
                }
            }
        }
    }
}






@Composable
fun FoodCard(title: String, price: String, rating: Double, color: Color) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = color,
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .width(200.dp)
            .wrapContentHeight()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = painterResource(id = R.drawable.fries), // Replace accordingly
                contentDescription = title,
                modifier = Modifier.size(48.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Text(text = "Free delivery", fontSize = 12.sp, color = Color.White)
            Text(text = price, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)

            Spacer(modifier = Modifier.height(4.dp))

            Button(
                onClick = { /* handle buy */ },
                //  colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(text = "Buy now", color = Color.White)
            }
        }
    }
}

data class Offer(
    val title: String,
    val description: String,
    val color: Color
)


