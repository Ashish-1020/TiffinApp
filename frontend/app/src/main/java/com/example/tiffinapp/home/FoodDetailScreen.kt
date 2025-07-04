package com.example.tiffinapp.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.tiffinapp.R
/*
@Composable
fun FoodDetailScreen(mealId:String,navController:NavController) {
    val viewModel: HomeViewModel = hiltViewModel()
    val meal = viewModel.meal.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getMealbyId(mealId)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        val scrollState = rememberScrollState()
        Column(modifier = Modifier
            .verticalScroll(scrollState)) {

        // Top image with back button
        Box {
            AsyncImage(
                model = meal.value.imgurl,
                contentDescription = meal.value.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(400.dp)
            )


            IconButton(
                onClick = {  navController.popBackStack() },
                modifier = Modifier
                    .padding(16.dp)
                    .background(Color.White.copy(alpha = 0.9f), shape = CircleShape)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
        }

        Surface(
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = Color.White,
            shadowElevation = 8.dp,
            modifier = Modifier
                .fillMaxSize()
                .offset(y = (-24).dp)
        ) {
            Column(modifier = Modifier
                .padding(24.dp)) {
                // Category
                Text("American, fast food", color = Color.Gray, fontSize = 14.sp)

                // Name + Price
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Mega Stack Burger", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text(
                        text = "$34.00",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Location & delivery info
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Place, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Milan, Italy • Free delivery", color = Color.Gray, fontSize = 13.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Stats: Time, Reviews, Rating

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFF5F5F5), // Light gray background
                    tonalElevation = 2.dp,
                    modifier = Modifier
                        .fillMaxWidth()

                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp, horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        InfoPill(icon = Icons.Default.AccessTime, label = "10 min", sub = "Delivery")

                        VerticalDivider()

                        InfoPill(icon = Icons.Default.People, label = "2k+", sub = "Reviews")

                        VerticalDivider()

                        InfoPill(icon = Icons.Default.Star, label = "4.6", sub = "Rating", iconTint = Color(0xFFFFC107))
                    }
                }





                Spacer(modifier = Modifier.height(16.dp))

                // Description
                Text("Description", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Behold the Mega Stack Burger, a towering masterpiece of flavour! Layers of succulent beef patties, smoky bacon, gooey cheddar...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(16.dp))


                var selected by remember { mutableStateOf("250 Gms") }

                QuantitySelector(
                    selectedQuantity = selected,
                    onQuantitySelected = { selected = it }
                )

                Spacer(modifier = Modifier.height(24.dp))

                val mealComponents = listOf(
                    "Beef Patty" to "2 pcs",
                    "Cheddar Cheese" to "1 slice",
                    "Lettuce" to "2 leaves",
                    "Tomato" to "2 slices",
                    "Onion" to "3 rings",
                    "Sauce" to "1 tbsp",
                    "Bun" to "1"
                )

                MealInfoTable(ingredients = mealComponents)


                // Quantity + Add to cart
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Quantity selector
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(12.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("1", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(20.dp))
                    }

                    // Add to Cart button
                    Button(
                        onClick = { /* Add to cart */ },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5722))
                    ) {
                        Text("Add to cart", color = Color.White)
                    }
                }
            }
        }
        }
    }
}*/


@Composable
fun FoodDetailScreen(mealId: String, navController: NavController) {
    val viewModel: HomeViewModel = hiltViewModel()
    val meal by viewModel.meal.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getMealbyId(mealId)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        val scrollState = rememberScrollState()
        Column(modifier = Modifier.verticalScroll(scrollState)) {

            // Top image with back button
            Box {
                AsyncImage(
                    model = meal.imgurl,
                    contentDescription = meal.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(400.dp)
                )

                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .padding(16.dp)
                        .background(Color.White.copy(alpha = 0.9f), shape = CircleShape)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }

            Surface(
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                color = Color.White,
                shadowElevation = 8.dp,
                modifier = Modifier
                    .fillMaxSize()
                    .offset(y = (-24).dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {


                    // Name + Price
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            meal.name,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "₹${(meal.price * (100 - meal.offer)) / 100}",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Location & delivery info
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Place, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text( "Lucknow • Free delivery", color = Color.Gray, fontSize = 13.sp)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Stats: Time, Reviews, Rating
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFFF5F5F5),
                        tonalElevation = 2.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp, horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            InfoPill(icon = Icons.Default.AccessTime, label = "25 min", sub = "Delivery")
                            VerticalDivider()
                            InfoPill(icon = Icons.Default.People, label = "${meal.noofreviews}+", sub = "Reviews")
                            VerticalDivider()
                            InfoPill(icon = Icons.Default.Star, label = "${meal.rating}", sub = "Rating", iconTint = Color(0xFFFFC107))
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Description
                    Text("Description", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = meal.description ?: "No description available.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )


                    Spacer(modifier = Modifier.height(24.dp))

                    // Ingredients list parsing (if string)
                    val mealComponents = meal.ingredients?.map {
                        it.name to it.quantity
                    } ?: listOf(
                        "Beef Patty" to "2 pcs",
                        "Cheddar Cheese" to "1 slice",
                        "Lettuce" to "2 leaves",
                        "Tomato" to "2 slices",
                        "Onion" to "3 rings",
                        "Sauce" to "1 tbsp",
                        "Bun" to "1"
                    )

                    MealInfoTable(ingredients = mealComponents)

                    // Quantity + Add to cart
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(12.dp))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = null, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("1", fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(20.dp))
                        }

                        Button(
                            onClick = { /* Add to cart */ },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5722))
                        ) {
                            Text("Add to cart", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun InfoPill(icon: ImageVector, label: String, sub: String, iconTint: Color = Color.Black) {
    Column(
        modifier = Modifier
            .padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(label, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
        Text(sub, fontSize = 12.sp, color = Color.Gray)
    }
}


@Composable
fun VerticalDivider() {
    Box(
        modifier = Modifier
            .height(40.dp)
            .width(1.dp)
            .background(Color.LightGray.copy(alpha = 0.5f))
    )
}



@Composable
fun MealInfoTable(ingredients: List<Pair<String, String>>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Ingredient",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Quantity",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Divider
        Divider(color = Color.LightGray)

        // Items
        ingredients.forEach { (ingredient, quantity) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = ingredient, style = MaterialTheme.typography.bodyMedium)
                Text(text = quantity, style = MaterialTheme.typography.bodyMedium)
            }

            Divider(color = Color.LightGray.copy(alpha = 0.2f))
        }
    }
}