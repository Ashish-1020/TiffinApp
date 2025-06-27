package com.example.tiffinapp.home

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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WatchLater
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
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
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.withStyle
import androidx.compose.material3.*
import androidx.compose.ui.text.style.TextDecoration
import androidx.navigation.NavController

@Composable
fun FoodHomeScreen(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        PromoScreen()
        HeaderSection()
        FoodCategorySection()
        FoodItemCard()
       // PopularFoodsSection()

    }
}


@Composable
fun PromoScreen() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFDF2F2))
            .padding(16.dp)
    ) {
        // Location and Bell Icon
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Place, contentDescription = null, tint = Color.Red)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Albuquerque, NM", fontWeight = FontWeight.Bold)
            }
            Icon(Icons.Default.Notifications, contentDescription = null)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Search Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(50))
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Search delivo™", color = Color.Gray, modifier = Modifier.weight(1f))
            Icon(Icons.Default.Mic, contentDescription = null, tint = Color.Red)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Offer Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFFFF5722))
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = "Use code FIRST50 at checkout.\nHurry, offer ends soon!",
                    color = Color.White,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Get 50% Off\nYour First Order!",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = { /* TODO */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                    shape = RoundedCornerShape(50)
                ) {
                    Text("Order Now", color = Color.White)
                }
            }

            // Example food image overlay (replace with Image if using real assets)
            Image(
                painter = painterResource(id = R.drawable.handpizza), // Replace with your image
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(200.dp)
            )
        }
    }
}

@Composable
fun HeaderSection() {
    Column(modifier = Modifier.padding(16.dp)) {
      /*  Image(
            painter = painterResource(id = R.drawable.handpizza), // Replace with your image
            contentDescription = "Dish Image",
            modifier = Modifier
                .size(240.dp)
                .align(Alignment.CenterHorizontally)
        )*/

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color.Gray)) { append("A ") }
                withStyle(style = SpanStyle(color = Color(0xFFFF9800), fontWeight = FontWeight.Bold)) { append("special dish ") }
                withStyle(style = SpanStyle(color = Color.Black, fontWeight = FontWeight.Bold)) { append("prepared for you") }
            },
            fontSize = 22.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )

        Text(
            text = "Our food delivery app brings your favourite dishes to you.",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 4.dp).align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun FoodCategorySection() {
    val categories = listOf("Burger", "Pizza", "Fries", "Drinks", "Meat")
    val icons = listOf(
        R.drawable.ic_sample_item, R.drawable.pizza, R.drawable.fries,
        R.drawable.drinks, R.drawable.meat
    )

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(categories.size) { index ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Card(
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(
                        containerColor = if (index == 2) Color(0xFFFF9800) else Color(0xFFF5F5F5),
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
fun PopularFoodsSection() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Popular Foods",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(2) { index ->
                FoodCard(
                    title = if (index == 0) "French Fries" else "Salad",
                    price = if (index == 0) "$24.00" else "$16.00",
                    rating = if (index == 0) 4.8 else 4.5,
                    color = if (index == 0) Color(0xFFFF7043) else Color(0xFF66BB6A)
                )
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
            .wrapContentHeight(),
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


@Composable
fun FoodItemCard() {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        modifier = Modifier
            .width(200.dp)
            .padding(8.dp)
    ) {
        Column {
            Box(modifier = Modifier.height(120.dp)) {
                Image(
                    painter = painterResource(id = R.drawable.handpizza), // Replace with your image
                    contentDescription = "Veggie Wrap",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth()
                )

                // Time Tag
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                        .background(Color.White, shape = RoundedCornerShape(12.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.WatchLater,
                        contentDescription = "Time",
                        tint = Color.Gray,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("25 min", fontSize = 12.sp, color = Color.Gray)
                }

                // Heart Icon
                IconButton(
                    onClick = { /* Add to favorite */ },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .background(Color.White.copy(alpha = 0.9f), shape = CircleShape)
                        .size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = Color.Red
                    )
                }
            }

            Column(modifier = Modifier.padding(12.dp)) {
                Text("Veggie Wrap", fontWeight = FontWeight.Bold, fontSize = 16.sp)

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Rating section
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("4.5", fontSize = 13.sp)
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Rating",
                            tint = Color(0xFFFFC107),
                            modifier = Modifier.size(16.dp)
                        )
                        Text("(18+)", fontSize = 13.sp, color = Color.Gray)
                    }

                    // Calorie section
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment, // 🔥 icon
                            contentDescription = "Calories",
                            tint = Color(0xFFFF5722),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "340 kcal",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }


                /*Text(
                    text = "$0 Delivery fee over $25",
                    fontSize = 12.sp,
                    color = Color.Red
                )*/

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // PRICE STACK
                    Column {
                        // Current price (bold)
                        Text(
                            text = "$7.99", // menuItem.price
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )

                        // Original price (strikethrough)
                        Text(
                            text = "$10.00", // menuItem.originalPrice
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            textDecoration = TextDecoration.LineThrough
                        )
                    }

                    // Discount badge + Add button
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Discount tag
                        Surface(
                            color = Color(0xFFD32F2F), // Or your TiffinColors.DiscountRed
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "20% OFF", // menuItem.discount
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        // Add to cart button




                IconButton(
                        onClick = { /* Add to cart */ },
                        modifier = Modifier
                            .background(Color(0xFFFF5722), shape = CircleShape)
                            .size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
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




@Composable
fun PromoScreenM3() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(16.dp)
    ) {
        // Location and Notification
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Place,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Albuquerque, NM", fontWeight = FontWeight.Bold)
            }
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Search Bar (replace with SearchBar in M3 if needed)
        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("Search delivo™") },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = null)
            },
            trailingIcon = {
                Icon(imageVector = Icons.Default.Mic, contentDescription = null)
            },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp)),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Offer Card
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(Modifier.padding(16.dp)) {
                Column {
                    Text(
                        "Use code FIRST50 at checkout.\nHurry, offer ends soon!",
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Get 50% Off\nYour First Order!",
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { /* TODO */ },
                        shape = RoundedCornerShape(50)
                    ) {
                        Text("Order Now")
                    }
                }
            }
        }
    }
}
}

