@file:OptIn(ExperimentalFoundationApi::class)

package com.example.tiffinapp.ui.home

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiffinapp.R
import com.example.tiffinapp.ui.theme.TiffinAppTheme
import kotlinx.coroutines.delay

// Data Classes
data class MenuItem(
    val id: String,
    val name: String,
    val price: String,
    val originalPrice: String? = null,
    val calories: Int,
    val isVeg: Boolean,
    val category: String,
    val description: String,
    val rating: Float = 0f,
    val imageRes: Int? = null,
    val imageUrl: String? = null,
    val isPopular: Boolean = false,
    val discount: Int? = null
)

data class QuickAction(
    val title: String,
    val icon: ImageVector,
    val backgroundColor: Color,
    val iconColor: Color = Color.White
)

// Theme Colors
object TiffinColors {
    val VegGreen = Color(0xFF4CAF50)
    val NonVegRed = Color(0xFFF44336)
    val AccentOrange = Color(0xFFFF9800)
    val RatingGold = Color(0xFFFFC107)
    val DiscountRed = Color(0xFFE53935)
}

val GreenPrimary = Color(0xFF2E7D32)
val LightGrayBorder = Color(0xFFE0E0E0)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TiffinHomeScreen(
    menuItems: List<MenuItem> = getSampleMenuItems(),
    onMenuItemClick: (MenuItem) -> Unit = {},
    onAddToCart: (MenuItem) -> Unit = {},
    onSearchQuery: (String) -> Unit = {},
    onQuickActionClick: (QuickAction) -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var isVegMode by remember { mutableStateOf(false) }

    val filteredItems = remember(menuItems, isVegMode, searchQuery) {
        menuItems.filter { item ->
            val matchesVegMode = if (isVegMode) item.isVeg else true
            val matchesSearch = if (searchQuery.isBlank()) true else
                item.name.contains(searchQuery, ignoreCase = true) ||
                        item.category.contains(searchQuery, ignoreCase = true) ||
                        item.description.contains(searchQuery, ignoreCase = true)
            matchesVegMode && matchesSearch
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.background.copy(alpha = 0.95f)
                    )
                )
            ),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        // Top Section - Fixed Header
        item {
            TopHeaderSection(
                isVegMode = isVegMode,
                onVegModeToggle = { isVegMode = it },
                searchQuery = searchQuery,
                onSearchQueryChange = {
                    searchQuery = it
                    onSearchQuery(it)
                }
            )
        }

        // Quick Actions Row
        item {
            QuickActionsSection(
                onQuickActionClick = onQuickActionClick
            )
        }

        // Section Header
        item {
            SectionHeader(
                title = if (isVegMode) "Vegetarian Meals" else "Today's Menu",
                subtitle = "${filteredItems.size} items available"
            )
        }

        // Menu Items
        items(
            items = filteredItems,
            key = { it.id }
        ) { menuItem ->
            EnhancedMenuItemCard(
                menuItem = menuItem,
                onItemClick = { onMenuItemClick(menuItem) },
                onAddToCart = { onAddToCart(menuItem) },
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    //.animateItemPlacement()
            )
        }

        // Bottom Spacing
        item {
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopHeaderSection(
    isVegMode: Boolean,
    onVegModeToggle: (Boolean) -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .statusBarsPadding()
        ) {
            // Header Row with Location and Veg Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Location Section
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = "Delivering to",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            fontWeight = FontWeight.Normal
                        )
                        Text(
                            text = "JL. Kampung Melon No. 32",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Veg Mode Toggle
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(
                            color = if (isVegMode) TiffinColors.VegGreen.copy(alpha = 0.1f)
                            else MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "Veg Mode",
                        tint = if (isVegMode) TiffinColors.VegGreen else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Veg Only",
                        fontSize = 11.sp,
                        color = if (isVegMode) TiffinColors.VegGreen else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Switch(
                        checked = isVegMode,
                        onCheckedChange = onVegModeToggle,
                        modifier = Modifier.scale(0.8f),
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                            checkedTrackColor = TiffinColors.VegGreen,
                            uncheckedThumbColor = MaterialTheme.colorScheme.onSurface,
                            uncheckedTrackColor = MaterialTheme.colorScheme.outline
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "What do you feel like eating?",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        fontSize = 14.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                },
                trailingIcon = if (searchQuery.isNotEmpty()) {
                    {
                        IconButton(
                            onClick = { onSearchQueryChange("") }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear",
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                } else null,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(16.dp),
                singleLine = true
            )
        }
    }
}

@Composable
fun QuickActionsSection(
    onQuickActionClick: (QuickAction) -> Unit
) {
    val quickActions = listOf(
        QuickAction("Big Promo", Icons.Default.LocationOn, TiffinColors.AccentOrange),
        QuickAction("Your Picks", Icons.Default.Favorite, Color(0xFFE91E63)),
        QuickAction("Today's Menu", Icons.Default.Menu, MaterialTheme.colorScheme.primary),
        QuickAction("Weekly Planner", Icons.Default.DateRange, Color(0xFF2196F3))
    )

    Column(
        modifier = Modifier.padding(vertical = 20.dp)
    ) {
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(quickActions) { action ->
                QuickActionButton(
                    action = action,
                    onClick = { onQuickActionClick(action) }
                )
            }
        }
    }
}

@Composable
fun QuickActionButton(
    action: QuickAction,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "scale"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .scale(scale)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onClick()
            }
    ) {
        ElevatedCard(
            modifier = Modifier
                .size(70.dp),
            shape = CircleShape,
            colors = CardDefaults.elevatedCardColors(
                containerColor = action.backgroundColor
            ),
            elevation = CardDefaults.elevatedCardElevation(
                defaultElevation = 6.dp,
                pressedElevation = 2.dp
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = action.icon,
                    contentDescription = action.title,
                    tint = action.iconColor,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = action.title,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun SectionHeader(
    title: String,
    subtitle: String
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )
    }
}

@Composable
fun EnhancedMenuItemCard(
    menuItem: MenuItem,
    onItemClick: () -> Unit,
    onAddToCart: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isAddingToCart by remember { mutableStateOf(false) }

    // Animation for add to cart
    LaunchedEffect(isAddingToCart) {
        if (isAddingToCart) {
            delay(300)
            isAddingToCart = false
        }
    }

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onItemClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp
        )
    ) {
        Box {
            // Background gradient overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                                Color.Transparent
                            )
                        )
                    )
            )

            Row(
                modifier = Modifier.padding(16.dp)
            ) {
                // Left Content
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    // Top Row: Category, Veg/Non-Veg, Rating
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Category with badges
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (menuItem.isPopular) {
                                Surface(
                                    color = TiffinColors.AccentOrange,
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.padding(end = 6.dp)
                                ) {
                                    Text(
                                        text = "POPULAR",
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Surface(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = menuItem.category,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Rating
                            if (menuItem.rating > 0) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Rating",
                                    tint = TiffinColors.RatingGold,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = menuItem.rating.toString(),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(start = 2.dp, end = 8.dp)
                                )
                            }

                            // Veg/Non-Veg Indicator
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .border(
                                        width = 1.5.dp,
                                        color = if (menuItem.isVeg) TiffinColors.VegGreen else TiffinColors.NonVegRed,
                                        shape = RoundedCornerShape(2.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(
                                            color = if (menuItem.isVeg) TiffinColors.VegGreen else TiffinColors.NonVegRed,
                                            shape = CircleShape
                                        )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Dish Name
                    Text(
                        text = menuItem.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Description
                    Text(
                        text = menuItem.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Calories
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Image(
                            painter = painterResource(id = R.drawable.ic_calories),
                            contentDescription = "Calories",
                            modifier = Modifier.size(16.dp),
                            colorFilter = ColorFilter.tint(TiffinColors.AccentOrange)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${menuItem.calories} kcal",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Bottom Row: Price, View Details, Add to Cart
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Price Section
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = menuItem.price,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )

                                if (menuItem.originalPrice != null) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = menuItem.originalPrice,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                        textDecoration = TextDecoration.LineThrough
                                    )
                                }

                                if (menuItem.discount != null) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        color = TiffinColors.DiscountRed,
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = "${menuItem.discount}% OFF",
                                            fontSize = 8.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                        )
                                    }
                                }
                            }

                            // View Details Link
                            Text(
                                text = "View Details",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary,
                                textDecoration = TextDecoration.Underline,
                                modifier = Modifier
                                    .clickable { onItemClick() }
                                    .padding(vertical = 2.dp)
                            )
                        }

                        // Add to Cart Button
                        AnimatedAddToCartButton(
                            isAdding = isAddingToCart,
                            onClick = {
                                isAddingToCart = true
                                onAddToCart()
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Dish Image
                ElevatedCard(
                    modifier = Modifier.size(100.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.surfaceVariant,
                                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_calories),
                            contentDescription = "Calories",
                            modifier = Modifier.size(40.dp),
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)),

                        )

                        // Placeholder for AsyncImage
                        // AsyncImage(
                        //     model = menuItem.imageUrl,
                        //     contentDescription = menuItem.name,
                        //     modifier = Modifier.fillMaxSize(),
                        //     contentScale = ContentScale.Crop,
                        //     placeholder = painterResource(R.drawable.food_placeholder),
                        //     error = painterResource(R.drawable.food_placeholder)
                        // )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedAddToCartButton(
    isAdding: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isAdding) 1.1f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )

    val backgroundColor by animateColorAsState(
        targetValue = if (isAdding) TiffinColors.VegGreen else MaterialTheme.colorScheme.primary,
        animationSpec = tween(200),
        label = "backgroundColor"
    )

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor
        ),
        shape = RoundedCornerShape(20.dp),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
        modifier = Modifier
            .scale(scale)
            .height(32.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = if (isAdding) 6.dp else 2.dp
        )
    ) {
        AnimatedContent(
            targetState = isAdding,
            transitionSpec = {
                fadeIn(animationSpec = tween(150)) with
                        fadeOut(animationSpec = tween(150))
            },
            label = "buttonContent"
        ) { adding ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = if (adding) Icons.Default.Check else Icons.Default.Add,
                    contentDescription = if (adding) "Added" else "Add to Cart",
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (adding) "Added!" else "Add",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

// Sample Data
fun getSampleMenuItems(): List<MenuItem> {
    return listOf(
        MenuItem(
            id = "1",
            name = "Butter Chicken with Naan",
            price = "₹120",
            originalPrice = "₹150",
            discount = 20,
            calories = 450,
            isVeg = false,
            category = "North Indian",
            description = "Creamy tomato-based curry with tender chicken pieces, served with fresh naan bread.",
            rating = 4.5f,
            isPopular = true
        ),
        MenuItem(
            id = "2",
            name = "Paneer Tikka Masala",
            price = "₹95",
            calories = 350,
            isVeg = true,
            category = "Vegetarian",
            description = "Grilled cottage cheese in rich tomato gravy with aromatic spices and herbs.",
            rating = 4.3f
        ),
        MenuItem(
            id = "3",
            name = "Chicken Biryani",
            price = "₹180",
            originalPrice = "₹200",
            discount = 10,
            calories = 520,
            isVeg = false,
            category = "Biryani",
            description = "Fragrant basmati rice cooked with tender chicken, saffron, and exotic spices.",
            rating = 4.7f,
            isPopular = true
        ),
        MenuItem(
            id = "4",
            name = "Masala Dosa",
            price = "₹65",
            calories = 280,
            isVeg = true,
            category = "South Indian",
            description = "Crispy rice and lentil crepe filled with spiced potato curry, served with chutney.",
            rating = 4.2f
        ),
        MenuItem(
            id = "5",
            name = "Fish Curry Rice",
            price = "₹140",
            calories = 380,
            isVeg = false,
            category = "South Indian",
            description = "Fresh fish cooked in coconut milk with traditional spices, served with steamed rice.",
            rating = 4.4f
        ),
        MenuItem(
            id = "6",
            name = "Rajma Chawal",
            price = "₹85",
            calories = 320,
            isVeg = true,
            category = "North Indian",
            description = "Red kidney beans curry cooked in rich tomato gravy, served with basmati rice.",
            rating = 4.1f
        )
    )
}

@Preview(showBackground = true)
@Composable
fun TiffinHomeScreenPreview() {
    TiffinAppTheme {
        TiffinHomeScreen()
    }
}

