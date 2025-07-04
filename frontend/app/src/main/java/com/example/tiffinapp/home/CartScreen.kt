package com.example.tiffinapp.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiffinapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen() {
    var cartItems by remember {
        mutableStateOf(
            listOf(
                CartItem("Small Margherita Pizza", "Cheese, Onion", 8.50, 1, R.drawable.pizza),
                CartItem("Medium Beef Pizza", "Mushroom", 9.50, 2, R.drawable.handpizza)
            )
        )
    }

    // Contact info state
    var contactName by remember { mutableStateOf("Ashish Yadav") }
    var contactPhone by remember { mutableStateOf("+91 9876543210") }

    // Address state
    var deliveryAddress by remember { mutableStateOf("94311 Meagan Inlet Suite 386") }

    // Payment state
    var selectedPaymentMethod by remember { mutableStateOf("Cash") }

    // Bottom sheet states
    var showContactSheet by remember { mutableStateOf(false) }
    var showAddressSheet by remember { mutableStateOf(false) }
    var showPaymentSheet by remember { mutableStateOf(false) }

    // Bottom sheet state objects
    val contactSheetState = rememberModalBottomSheetState()
    val addressSheetState = rememberModalBottomSheetState()
    val paymentSheetState = rememberModalBottomSheetState()

    // Calculate totals
    val subtotal = cartItems.sumOf { it.price * it.quantity }
    val delivery = 2.00
    val tax = 2.00
    val total = subtotal + delivery + tax

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text(
            text = "My Order",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        cartItems.forEachIndexed { index, item ->
            CartItemCard(
                item = item,
                onQuantityChange = { newQuantity ->
                    cartItems = cartItems.toMutableList().apply {
                        this[index] = this[index].copy(quantity = maxOf(1, newQuantity))
                    }
                }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        PriceSummarySection(
            subtotal = subtotal,
            delivery = delivery,
            tax = tax,
            total = total
        )

        Spacer(modifier = Modifier.height(16.dp))

        AddressAndPayment(
            contactName = contactName,
            contactPhone = contactPhone,
            deliveryAddress = deliveryAddress,
            paymentMethod = selectedPaymentMethod,
            onContactClick = { showContactSheet = true },
            onAddressClick = { showAddressSheet = true },
            onPaymentClick = { showPaymentSheet = true }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { /* Place order */ },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Place Order",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }

    // Contact Bottom Sheet
    if (showContactSheet) {
        ModalBottomSheet(
            onDismissRequest = { showContactSheet = false },
            sheetState = contactSheetState,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            var tempName by remember { mutableStateOf(contactName) }
            var tempPhone by remember { mutableStateOf(contactPhone) }

            ContactInfoSelector(
                name = tempName,
                phone = tempPhone,
                address = deliveryAddress,
                onNameChange = { tempName = it },
                onPhoneChange = { tempPhone = it },
                onSubmit = {
                    contactName = tempName
                    contactPhone = tempPhone
                    showContactSheet = false
                }
            )
        }
    }

    // Address Bottom Sheet
    if (showAddressSheet) {
        ModalBottomSheet(
            onDismissRequest = { showAddressSheet = false },
            sheetState = addressSheetState,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            var tempAddress by remember { mutableStateOf(deliveryAddress) }

            AddressSelector(
                address = tempAddress,
                onAddressChange = { tempAddress = it },
                onSubmit = {
                    deliveryAddress = tempAddress
                    showAddressSheet = false
                }
            )
        }
    }

    // Payment Method Bottom Sheet
    if (showPaymentSheet) {
        ModalBottomSheet(
            onDismissRequest = { showPaymentSheet = false },
            sheetState = paymentSheetState,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            var tempPaymentMethod by remember { mutableStateOf(selectedPaymentMethod) }

            PaymentMethodSelector(
                selectedMethod = tempPaymentMethod,
                onMethodChange = { tempPaymentMethod = it },
                onSubmit = {
                    selectedPaymentMethod = tempPaymentMethod
                    showPaymentSheet = false
                }
            )
        }
    }
}

data class CartItem(val name: String, val addOn: String, val price: Double, val quantity: Int, val imageRes: Int)

@Composable
fun CartItemCard(item: CartItem, onQuantityChange: (Int) -> Unit) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.addOn,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$${String.format("%.2f", item.price)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }

            QuantitySelector(
                quantity = item.quantity,
                onQuantityChange = onQuantityChange
            )
        }
    }
}

@Composable
fun QuantitySelector(quantity: Int, onQuantityChange: (Int) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        IconButton(
            onClick = { onQuantityChange(quantity - 1) },
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                Icons.Default.Remove,
                contentDescription = "Decrease quantity",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(16.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = quantity.toString(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.width(12.dp))
        IconButton(
            onClick = { onQuantityChange(quantity + 1) },
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = "Increase quantity",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun PriceSummarySection(
    subtotal: Double,
    delivery: Double,
    tax: Double,
    total: Double
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            PriceRow("Subtotal", subtotal)
            PriceRow("Delivery Fee", delivery)
            PriceRow("Service Tax", tax)
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            PriceRow("Total", total, isBold = true, isTotal = true)
        }
    }
}

@Composable
fun PriceRow(label: String, amount: Double, isBold: Boolean = false, isTotal: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = if (isTotal) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium,
            color = if (label == "Delivery Fee") MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal
        )
        Text(
            text = "$${String.format("%.2f", amount)}",
            style = if (isTotal) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun AddressAndPayment(
    contactName: String,
    contactPhone: String,
    deliveryAddress: String,
    paymentMethod: String,
    onContactClick: () -> Unit = {},
    onAddressClick: () -> Unit = {},
    onPaymentClick: () -> Unit = {}
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Contact Info Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onContactClick() }
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Contact Info",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "$contactName • $contactPhone",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                }
                Icon(
                    Icons.Default.KeyboardArrowRight,
                    contentDescription = "Edit Contact",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Delivery Address Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onAddressClick() }
            ) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Your Delivery Address",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = deliveryAddress,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                }
                Icon(
                    Icons.Default.KeyboardArrowRight,
                    contentDescription = "Edit Address",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Payment Method Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onPaymentClick() }
            ) {
                Icon(
                    Icons.Default.AttachMoney,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Payment method",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = paymentMethod,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                }
                Icon(
                    Icons.Default.KeyboardArrowRight,
                    contentDescription = "Edit Payment",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactInfoSelector(
    name: String,
    phone: String,
    address: String,
    onNameChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onSubmit: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        // Header
        Text(
            text = "Update receiver details",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Home - $address",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Receiver name field
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text("Receiver's name") },
            trailingIcon = {
                Row {
                    if (name.isNotEmpty()) {
                        IconButton(onClick = { onNameChange("") }) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Clear",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    IconButton(onClick = { /* Contact picker */ }) {
                        Icon(
                            Icons.Default.Contacts,
                            contentDescription = "Pick from contacts",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                cursorColor = MaterialTheme.colorScheme.primary,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Phone number field
        OutlinedTextField(
            value = phone,
            onValueChange = onPhoneChange,
            label = { Text("Receiver's mobile number") },
            trailingIcon = {
                if (phone.isNotEmpty()) {
                    IconButton(onClick = { onPhoneChange("") }) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Clear",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                cursorColor = MaterialTheme.colorScheme.primary,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Submit button
        Button(
            onClick = onSubmit,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(
                text = "Submit",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressSelector(
    address: String,
    onAddressChange: (String) -> Unit,
    onSubmit: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        // Header
        Text(
            text = "Update delivery address",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Enter your complete delivery address",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Address field
        OutlinedTextField(
            value = address,
            onValueChange = onAddressChange,
            label = { Text("Delivery Address") },
            trailingIcon = {
                if (address.isNotEmpty()) {
                    IconButton(onClick = { onAddressChange("") }) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Clear",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                cursorColor = MaterialTheme.colorScheme.primary,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Submit button
        Button(
            onClick = onSubmit,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(
                text = "Update Address",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun PaymentMethodSelector(
    selectedMethod: String,
    onMethodChange: (String) -> Unit,
    onSubmit: () -> Unit
) {
    val paymentMethods = listOf("Cash", "Credit Card", "Debit Card", "UPI", "Net Banking")

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        // Header
        Text(
            text = "Select payment method",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Choose your preferred payment method",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Payment methods
        paymentMethods.forEach { method ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onMethodChange(method) }
                    .padding(vertical = 8.dp)
            ) {
                RadioButton(
                    selected = selectedMethod == method,
                    onClick = { onMethodChange(method) },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = MaterialTheme.colorScheme.primary,
                        unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = when (method) {
                        "Cash" -> Icons.Default.AttachMoney
                        "Credit Card", "Debit Card" -> Icons.Default.CreditCard
                        "UPI" -> Icons.Default.AccountBalance
                        "Net Banking" -> Icons.Default.AccountBalance
                        else -> Icons.Default.AttachMoney
                    },
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = method,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Submit button
        Button(
            onClick = onSubmit,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(
                text = "Update Payment Method",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}