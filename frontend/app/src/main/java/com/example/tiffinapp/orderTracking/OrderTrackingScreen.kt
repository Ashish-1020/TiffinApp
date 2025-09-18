package com.example.tiffinapp.orderTracking


import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.sin

// Data Models (same as before)
data class Order(
    val id: String,
    val restaurantName: String,
    val items: List<OrderItem>,
    val totalAmount: Double,
    val orderDate: Long,
    val status: OrderStatus,
    val deliveryAddress: String,
    val phases: List<DeliveryPhase>
)

data class OrderItem(
    val name: String,
    val quantity: Int,
    val price: Double,
    val image: String? = null
)

data class DeliveryPhase(
    val phase: Phase,
    val isCompleted: Boolean,
    val timestamp: Long? = null,
    val estimatedTime: String? = null
)

enum class Phase(val displayName: String, val description: String, val icon: ImageVector, val emoji: String) {
    PREPARATION("Preparing", "Chef is cooking your meal", Icons.Filled.Restaurant, "👨‍🍳"),
    PACKING("Packing", "Getting your order ready", Icons.Filled.Inventory2, "📦"),
    PICKED_UP("On the way", "Rider is coming to you", Icons.Filled.DeliveryDining, "🏍️"),
    DELIVERED("Delivered", "Enjoy your meal!", Icons.Filled.CheckCircle, "🎉")
}

enum class OrderStatus {
    ACTIVE,
    COMPLETED,
    CANCELLED
}

// Custom Colors
val PrimaryPurple = Color(0xFF6C5CE7)
val SecondaryPink = Color(0xFFFF6B9D)
val AccentOrange = Color(0xFFFF9500)
val SuccessGreen = Color(0xFF00D68F)
val LightGray = Color(0xFFF8F9FA)
val DarkGray = Color(0xFF2D3436)

// Sample Data
val sampleOrders = listOf(
    Order(
        id = "ORD001",
        restaurantName = "Bella Pizza 🍕",
        items = listOf(
            OrderItem("Margherita Pizza", 1, 12.99),
            OrderItem("Garlic Bread", 1, 4.99),
            OrderItem("Coke", 2, 2.99)
        ),
        totalAmount = 20.97,
        orderDate = System.currentTimeMillis(),
        status = OrderStatus.ACTIVE,
        deliveryAddress = "123 Main St, Downtown",
        phases = listOf(
            DeliveryPhase(Phase.PREPARATION, true, System.currentTimeMillis() - 900000, null),
            DeliveryPhase(Phase.PACKING, true, System.currentTimeMillis() - 300000, null),
            DeliveryPhase(Phase.PICKED_UP, false, null, "8 mins"),
            DeliveryPhase(Phase.DELIVERED, false, null, "15 mins")
        )
    ),
    Order(
        id = "ORD002",
        restaurantName = "Burger Junction 🍔",
        items = listOf(
            OrderItem("Double Cheeseburger", 1, 8.99),
            OrderItem("Fries", 1, 3.99)
        ),
        totalAmount = 12.98,
        orderDate = System.currentTimeMillis() - 86400000,
        status = OrderStatus.COMPLETED,
        deliveryAddress = "456 Oak Avenue",
        phases = listOf(
            DeliveryPhase(Phase.PREPARATION, true, System.currentTimeMillis() - 86400000 + 300000, null),
            DeliveryPhase(Phase.PACKING, true, System.currentTimeMillis() - 86400000 + 600000, null),
            DeliveryPhase(Phase.PICKED_UP, true, System.currentTimeMillis() - 86400000 + 900000, null),
            DeliveryPhase(Phase.DELIVERED, true, System.currentTimeMillis() - 86400000 + 1800000, null)
        )
    ),
    Order(
        id = "ORD003",
        restaurantName = "Sushi Zen 🍣",
        items = listOf(
            OrderItem("Dragon Roll", 1, 14.99),
            OrderItem("Miso Soup", 1, 3.99)
        ),
        totalAmount = 18.98,
        orderDate = System.currentTimeMillis() - 259200000,
        status = OrderStatus.COMPLETED,
        deliveryAddress = "789 Pine Street",
        phases = listOf(
            DeliveryPhase(Phase.PREPARATION, true, System.currentTimeMillis() - 259200000 + 300000, null),
            DeliveryPhase(Phase.PACKING, true, System.currentTimeMillis() - 259200000 + 600000, null),
            DeliveryPhase(Phase.PICKED_UP, true, System.currentTimeMillis() - 259200000 + 900000, null),
            DeliveryPhase(Phase.DELIVERED, true, System.currentTimeMillis() - 259200000 + 1500000, null)
        )
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderTrackingScreen() {
    var orders by remember { mutableStateOf(sampleOrders) }

    // Auto-update simulation
    LaunchedEffect(Unit) {
        while (true) {
            delay(30000)
            orders = orders.map { order ->
                if (order.status == OrderStatus.ACTIVE) {
                    val updatedPhases = order.phases.toMutableList()
                    val currentPhaseIndex = updatedPhases.indexOfFirst { !it.isCompleted }
                    if (currentPhaseIndex != -1 && currentPhaseIndex < updatedPhases.size - 1) {
                        if (Math.random() < 0.4) {
                            updatedPhases[currentPhaseIndex] = updatedPhases[currentPhaseIndex].copy(
                                isCompleted = true,
                                timestamp = System.currentTimeMillis()
                            )
                        }
                    }
                    order.copy(phases = updatedPhases)
                } else {
                    order
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        PrimaryPurple.copy(alpha = 0.1f),
                        Color.White
                    )
                )
            )
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header
            item {
               headersection()
            }

            // Active Order
            val activeOrder = orders.find { it.status == OrderStatus.ACTIVE }
            if (activeOrder != null) {
                item {
                    ActiveOrderCard(order = activeOrder)
                }
            }

            // Order History
            val completedOrders = orders.filter { it.status == OrderStatus.COMPLETED }
            if (completedOrders.isNotEmpty()) {
                item {
                    Text(
                        text = "Recent Orders",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = DarkGray,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                items(completedOrders) { order ->
                    OrderHistoryCard(order = order)
                }
            }
        }
    }
}

@Composable
fun headersection(){
    val infiniteTransition = rememberInfiniteTransition(label = "wave")
    val animatedValue by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "wave"
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "🍽️ Order Tracking",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = DarkGray,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Track your delicious journey",
            style = MaterialTheme.typography.bodyLarge,
            color = DarkGray.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            modifier = Modifier.alpha(0.8f + 0.2f * sin(animatedValue * 2 * Math.PI).toFloat())
        )
    }
}

@Composable
fun ActiveOrderCard(order: Order) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 20.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = PrimaryPurple.copy(alpha = 0.3f)
            ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            // Header with restaurant
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Live Order",
                        style = MaterialTheme.typography.labelLarge,
                        color = PrimaryPurple,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = order.restaurantName,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = DarkGray
                    )
                }

                // Live indicator
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(SuccessGreen, Color(0xFF00B894))
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    LiveDot()
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "LIVE",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Beautiful Timeline
            BeautifulTimeline(phases = order.phases)

            Spacer(modifier = Modifier.height(20.dp))

            // Order info
            OrderInfoSection(order = order)
        }
    }
}

@Composable
fun LiveDot() {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "pulse"
    )

    Box(
        modifier = Modifier
            .size(8.dp)
            .clip(CircleShape)
            .background(Color.White)
            .then(Modifier.size((8 * scale).dp))
    )
}

@Composable
fun BeautifulTimeline(phases: List<DeliveryPhase>) {
    val currentPhaseIndex = phases.indexOfFirst { !it.isCompleted }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        phases.forEachIndexed { index, phase ->
            val isCompleted = phase.isCompleted
            val isCurrent = index == currentPhaseIndex
            val isUpcoming = index > currentPhaseIndex

            TimelinePhase(
                phase = phase,
                isCompleted = isCompleted,
                isCurrent = isCurrent,
                isUpcoming = isUpcoming,
                isLast = index == phases.size - 1
            )
        }
    }
}

@Composable
fun TimelinePhase(
    phase: DeliveryPhase,
    isCompleted: Boolean,
    isCurrent: Boolean,
    isUpcoming: Boolean,
    isLast: Boolean
) {
    val animatedScale by animateFloatAsState(
        targetValue = if (isCurrent) 1.1f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "scale"
    )

    val circleColor = when {
        isCompleted -> SuccessGreen
        isCurrent -> AccentOrange
        else -> Color(0xFFE0E0E0)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Timeline indicator
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Circle with icon
            Box(
                modifier = Modifier
                    .size((48 * animatedScale).dp)
                    .clip(CircleShape)
                    .background(
                        brush = if (isCompleted || isCurrent) {
                            Brush.radialGradient(
                                colors = listOf(
                                    circleColor,
                                    circleColor.copy(alpha = 0.8f)
                                )
                            )
                        } else {
                            Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFFE0E0E0),
                                    Color(0xFFE0E0E0)
                                )
                            )
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                when {
                    isCompleted -> {
                        Icon(
                            Icons.Filled.CheckCircle,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    isCurrent -> {
                        Text(
                            text = phase.phase.emoji,
                            fontSize = 20.sp
                        )
                    }
                    else -> {
                        Icon(
                            phase.phase.icon,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.7f),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Connecting line
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(3.dp)
                        .height(40.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = if (isCompleted) {
                                    listOf(SuccessGreen, SuccessGreen.copy(alpha = 0.3f))
                                } else {
                                    listOf(Color(0xFFE0E0E0), Color(0xFFE0E0E0))
                                }
                            ),
                            shape = RoundedCornerShape(2.dp)
                        )
                )
            }
        }

        Spacer(modifier = Modifier.width(20.dp))

        // Phase content
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = phase.phase.displayName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                color = when {
                    isCompleted -> SuccessGreen
                    isCurrent -> AccentOrange
                    else -> DarkGray.copy(alpha = 0.6f)
                }
            )

            Text(
                text = phase.phase.description,
                style = MaterialTheme.typography.bodyMedium,
                color = DarkGray.copy(alpha = 0.7f)
            )

            // Time info
            if (isCompleted && phase.timestamp != null) {
                Text(
                    text = "✓ ${formatTime(phase.timestamp)}",
                    style = MaterialTheme.typography.labelMedium,
                    color = SuccessGreen,
                    modifier = Modifier
                        .background(
                            SuccessGreen.copy(alpha = 0.1f),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                )
            } else if (isCurrent && phase.estimatedTime != null) {
                Text(
                    text = "⏱️ ${phase.estimatedTime}",
                    style = MaterialTheme.typography.labelMedium,
                    color = AccentOrange,
                    modifier = Modifier
                        .background(
                            AccentOrange.copy(alpha = 0.1f),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }
        }
    }
}

@Composable
fun OrderInfoSection(order: Order) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                LightGray,
                RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        // Order summary
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${order.items.size} items",
                style = MaterialTheme.typography.bodyLarge,
                color = DarkGray,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "$${String.format("%.2f", order.totalAmount)}",
                style = MaterialTheme.typography.titleMedium,
                color = PrimaryPurple,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Address
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Outlined.LocationOn,
                contentDescription = null,
                tint = DarkGray.copy(alpha = 0.6f),
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = order.deliveryAddress,
                style = MaterialTheme.typography.bodyMedium,
                color = DarkGray.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
fun OrderHistoryCard(order: Order) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Handle click */ },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Restaurant icon
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                SecondaryPink.copy(alpha = 0.2f),
                                PrimaryPurple.copy(alpha = 0.1f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = order.restaurantName.first().toString(),
                    style = MaterialTheme.typography.titleLarge,
                    color = PrimaryPurple,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Order info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = order.restaurantName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = DarkGray
                )
                Text(
                    text = formatDate(order.orderDate),
                    style = MaterialTheme.typography.bodyMedium,
                    color = DarkGray.copy(alpha = 0.6f)
                )
                Text(
                    text = "${order.items.size} items • $${String.format("%.2f", order.totalAmount)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = PrimaryPurple,
                    fontWeight = FontWeight.Medium
                )
            }

            // Status
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            SuccessGreen.copy(alpha = 0.1f),
                            RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "✓ Delivered",
                        style = MaterialTheme.typography.labelMedium,
                        color = SuccessGreen,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(
                    onClick = { /* Reorder */ },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = PrimaryPurple
                    )
                ) {
                    Text("Reorder")
                }
            }
        }
    }
}

// Helper Functions
private fun formatTime(timestamp: Long): String {
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

private fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM dd", Locale.getDefault())
    return sdf.format(Date(timestamp))
}