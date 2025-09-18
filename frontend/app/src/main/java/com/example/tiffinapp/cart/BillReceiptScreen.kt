package com.example.tiffinapp.cart
/*
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tiffinapp.cart.util.generateStyledInvoicePdf
import com.example.tiffinapp.core.data.CartItemDto
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun BillReceiptScreen(
    subtotal: Double,
    delivery: Double,
    tax: Double,
    total: Double,
    contactName: String,
    contactPhone: String,
    address: String,
    paymentMethod: String,
    cartItems: List<CartItemDto>,
    modifier: Modifier = Modifier
) {
    val viewModel = hiltViewModel<CartViewModel>()
    val context = LocalContext.current
    var list =remember { mutableListOf<CartItemDto>() }
    val date = remember { SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date()) }


    LaunchedEffect(Unit) {
        list=cartItems as MutableList<CartItemDto>

        viewModel.clearCart()
    }
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Tiffin App - Bill Receipt", fontWeight = FontWeight.Bold, fontSize = 22.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Date: $date", fontSize = 14.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(16.dp))

        // Customer Details
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            tonalElevation = 1.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Customer Details", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Name: $contactName")
                Text("Phone: $contactPhone")
                Text("Address: $address")
                Text("Payment: $paymentMethod")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🧾 Receipt Table
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            tonalElevation = 1.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Items", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Item", fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                    Text("Qty", fontWeight = FontWeight.SemiBold, modifier = Modifier.width(40.dp))
                    Text("Price", fontWeight = FontWeight.SemiBold, modifier = Modifier.width(60.dp))
                }

                Spacer(modifier = Modifier.height(4.dp))

                list.forEach {
                    val unitPrice = it.price - (it.offer * it.price) / 100
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(it.name, modifier = Modifier.weight(1f))
                        Text("${it.quantity}", modifier = Modifier.width(40.dp))
                        Text("₹%.2f".format(unitPrice * it.quantity), modifier = Modifier.width(60.dp))
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Price Summary
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            tonalElevation = 1.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                PriceRow("Subtotal", subtotal)
                PriceRow("Delivery Fee", delivery)
                PriceRow("Service Tax", tax)
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                PriceRow("Total", total, isBold = true, isTotal = true)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // PDF Button
        Button(
            onClick = {
                generateStyledInvoicePdf(
                    context = context,
                    invoiceNumber = "INV-${System.currentTimeMillis()}",
                    date = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date()),
                    dueDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Calendar.getInstance().apply {
                        add(Calendar.DATE, 7)
                    }.time),
                    customerName = contactName,
                    customerCompany = "Tiffin App",
                    customerAddress = address,
                    customerEmail = contactPhone,
                    items = list.map {
                        Triple(it.name, it.quantity, it.price - (it.offer * it.price) / 100)
                    }
                )


            },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Icon(Icons.Default.Receipt, contentDescription = "Download")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Download PDF")
        }
    }
}

*/