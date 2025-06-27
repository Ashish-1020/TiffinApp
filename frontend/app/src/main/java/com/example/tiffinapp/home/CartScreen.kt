package com.example.tiffinapp.home
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.LocationOn
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiffinapp.R
@Composable
fun CartScreen() {
    val cartItems = listOf(
        CartItem("Small Margherita Pizza", "Cheese, Onion", 8.50, 1, R.drawable.pizza),
        CartItem("Medium Beef Pizza", "Mushroom", 9.50, 2, R.drawable.handpizza)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9F9FB))
            .padding(16.dp)
    ) {
        Text("My Order", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        cartItems.forEach { item ->
            CartItemCard(item)
            Spacer(modifier = Modifier.height(12.dp))
        }

        PriceSummarySection()

        Spacer(modifier = Modifier.height(16.dp))

        AddressAndPayment()

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { /* Place order */ },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D0C22)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Place Order", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

data class CartItem(val name: String, val addOn: String, val price: Double, val quantity: Int, val imageRes: Int)

@Composable
fun CartItemCard(item: CartItem) {

        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = item.imageRes),
                contentDescription = item.name,
                modifier = Modifier
                    .size(128.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(item.name, maxLines = 1, overflow = TextOverflow.Ellipsis, fontWeight = FontWeight.Bold)
               // Text("Add: ${item.addOn}", color = Color.Gray, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "$ ${String.format("%.2f", item.price)}",
                    color = Color(0xFF0D0C22),
                    fontWeight = FontWeight.Bold
                )
            }

            QuantitySelector(item.quantity)
        }
    }


@Composable
fun QuantitySelector(quantity: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(Color(0xFFF3F4F6), shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Icon(Icons.Default.Remove, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(quantity.toString(), fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.width(8.dp))
        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
    }
}

@Composable
fun PriceSummarySection() {
    val subtotal = 27.50
    val delivery = 2.00
    val tax = 2.00
    val total = subtotal + delivery + tax

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        tonalElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            PriceRow("Subtotal", subtotal)
            PriceRow("Delivery Fee", delivery)
            PriceRow("Service Tax", tax)
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            PriceRow("Total", total, isBold = true, isTotal = true)
        }
    }
}

@Composable
fun PriceRow(label: String, amount: Double, isBold: Boolean = false, isTotal: Boolean = false) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(
            label,
            color = if (label == "Delivery Fee") Color(0xFFBDBDBD) else Color.Black,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal
        )
        Text(
            text = "$${String.format("%.2f", amount)}",
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
            fontSize = if (isTotal) 18.sp else 14.sp
        )
    }
}

@Composable
fun AddressAndPayment() {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        tonalElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Gray)
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text("Your Delivery Address", fontSize = 13.sp, color = Color.Gray)
                    Text("94311 Meagan Inlet Suite 386", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AttachMoney, contentDescription = null, tint = Color.Gray)
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text("Payment method", fontSize = 13.sp, color = Color.Gray)
                    Text("Cash", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
