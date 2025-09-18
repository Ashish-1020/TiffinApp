package com.example.tiffinapp.cart

import android.os.Build
import android.widget.Toast
import com.airbnb.lottie.compose.*
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.tiffinapp.R
import com.example.tiffinapp.cart.util.generateStyledInvoicePdf
import com.example.tiffinapp.core.data.CartItemDto

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(viewModel: CartViewModel = hiltViewModel(), navController: NavController, onOrderPlaced: () -> Unit) {
    val observedItems by viewModel.cartItems.observeAsState(emptyList())
    val isLoading by viewModel.loading.observeAsState(false)
    val error by viewModel.error.observeAsState()
    val userDetail by viewModel.userDetail.observeAsState()
    val userDetailfull by viewModel.userDetailfull.observeAsState()
    var showOrderSuccess by remember { mutableStateOf(false) }


    var cartItems by remember { mutableStateOf(observedItems) }
    var showReceipt by remember { mutableStateOf(false) }
    var  showInsuffcientDialog by remember { mutableStateOf(false) }

    var contactName by remember { mutableStateOf("") }
    var contactPhone by remember { mutableStateOf("") }
    var deliveryAddress by remember { mutableStateOf("") }
    var selectedPaymentMethod by remember { mutableStateOf("CASH") }

    var showContactSheet by remember { mutableStateOf(false) }
    var showAddressSheet by remember { mutableStateOf(false) }
    var showPaymentSheet by remember { mutableStateOf(false) }

    val contactSheetState = rememberModalBottomSheetState()
    val addressSheetState = rememberModalBottomSheetState()
    val paymentSheetState = rememberModalBottomSheetState()

    val scrollState = rememberScrollState()

    val context = LocalContext.current

    // Fetch cart + user detail on first render
    LaunchedEffect(Unit) {
        viewModel.fetchCart()

        viewModel.fetchUserDetail()
    }

    // When userDetail fetched, sync to states

    LaunchedEffect(userDetailfull) {
        userDetailfull?.let {
            contactPhone = it.phoneNumber
            deliveryAddress = it.address
            contactName = it.name // 👈 or fetch from JWT if backend provides name
        }
    }

    LaunchedEffect(observedItems) {
        cartItems = observedItems
    }

    val subtotal = cartItems.sumOf { (it.price - (it.offer * it.price) / 100) * it.quantity }


    var delivery = 0.0
    if(cartItems.isNotEmpty())
        delivery= 0.005 * subtotal
    val tax = 0.05 * subtotal
    val total = subtotal + delivery + tax

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("My Order", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
             }

        Spacer(Modifier.height(16.dp))


                cartItems.forEachIndexed { index, item ->
                    CartItemCard(
                        item = item,
                        onQuantityChange = { newQty ->
                            when {
                                newQty == 0 -> {
                                    viewModel.removeItem(item.mealId)
                                    cartItems = cartItems.toMutableList().apply { removeAt(index) }
                                }

                                newQty != item.quantity -> {
                                    cartItems = cartItems.toMutableList().apply {
                                        set(index, item.copy(quantity = newQty))
                                    }
                                }
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }



        PriceSummarySection(subtotal, delivery, tax, total)
        Spacer(Modifier.height(16.dp))

        AddressAndPayment(
            contactName,
            contactPhone,
            deliveryAddress,
            selectedPaymentMethod,
            onContactClick = { showContactSheet = true },
            onAddressClick = { showAddressSheet = true },
            onPaymentClick = { showPaymentSheet = true }
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.updateUserDetail(contactPhone, deliveryAddress)

                val mealsJson = Json.encodeToString(cartItems) // Use kotlinx.serialization
                val transactionId = "TXN" + System.currentTimeMillis()
                if(selectedPaymentMethod == "WALLET"){
                    viewModel.addBalanceToServer(-total) { success ->
                        if (success) {
                            viewModel.placeOrder(
                                listOfMeals = mealsJson,
                                totalCost = total,
                                transactionType = selectedPaymentMethod,
                                transactionId = transactionId,
                                name = contactName,
                                address = deliveryAddress,
                                mobileNo = contactPhone,
                                onSuccess = {
                                    viewModel.clearCart()
                                    cartItems = emptyList()
                                    Toast.makeText(context, "Order placed!", Toast.LENGTH_SHORT).show()
                                    showOrderSuccess = true
                                },
                                onFailure = {
                                    Toast.makeText(context, "Order failed: $it", Toast.LENGTH_LONG).show()
                                }
                            )
                        } else {
                            showInsuffcientDialog=true
                        }
                    }
                }else{
                viewModel.placeOrder(
                    listOfMeals = mealsJson,
                    totalCost = total,
                    transactionType = selectedPaymentMethod,
                    transactionId = transactionId,
                    name = contactName,
                    address = deliveryAddress,
                    mobileNo = contactPhone,
                    onSuccess = {


                        Toast.makeText(context, "Order placed!", Toast.LENGTH_SHORT).show()
                        showOrderSuccess = true

                    },
                    onFailure = {
                        Toast.makeText(context, "Order failed: $it", Toast.LENGTH_LONG).show()
                    }
                )
                }

            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
        ) {
            Text("Place Order", color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold)
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

    // Payment Bottom Sheet
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

    if (showOrderSuccess) {
        OrderSuccessDialog(
            estimatedTimeMinutes = 30,
            onTrackOrderClick = {
                showOrderSuccess = false
                onOrderPlaced()// or relevant route
                viewModel.clearCart()

            },
            onPrintReceiptClick = {
                showOrderSuccess = false
                showReceipt = true
                viewModel.clearCart()

            },
            onDismiss = { showOrderSuccess = false
                viewModel.clearCart()
                cartItems = emptyList()}

        )
    }


    if(showInsuffcientDialog){
        InsufficientFundsDialog(
            onAddMoneyClick = { /* Navigate to wallet top-up */ },
            onChangePaymentMethodClick = { /* Navigate to payment selection */ },
            onDismiss = { showInsuffcientDialog = false }
        )
    }

    if(showReceipt){
        val context = LocalContext.current

        generateStyledInvoicePdf(
            context = context,
            invoiceNumber = "INV-${System.currentTimeMillis()}",
            date = "2025-08-05",
            dueDate = "2025-08-10",
            customerName = contactName,
            customerCompany = "Tiffin Pvt Ltd",
            customerAddress = deliveryAddress,
            customerPhone = contactPhone,
            items = cartItems

        )
    }




}

@Composable
fun CartItemCard(item: CartItemDto, onQuantityChange: (Int) -> Unit) {
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
                Row(verticalAlignment = Alignment.CenterVertically){
                Text(
                    text = "\u20B9${String.format("%.2f", (item.price-(item.offer*item.price)/100))}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                    Spacer(modifier = Modifier.width(4.dp))
                    if (item.offer != 0) {
                        Surface(color = Color(0xFFD32F2F), shape = RoundedCornerShape(4.dp)) {
                            Text(
                                "${item.offer}% OFF",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))

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
            text = "\u20B9${String.format("%.2f", amount)}",
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
    val paymentMethods = listOf("CASH", "WALLET")

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
                        "Wallet", "Debit Card" -> Icons.Default.CreditCard
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




@Composable
fun OrderSuccessDialog(
    estimatedTimeMinutes: Int = 30,
    onTrackOrderClick: () -> Unit,
    onPrintReceiptClick: () -> Unit,
    onDismiss: () -> Unit
) {
    // Lottie Animation setup
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.sucess_anim))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 5, // play once
        speed = 1.0f,
        isPlaying = true
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.4f))
            .clickable(enabled = true, onClick = onDismiss),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Lottie Animation
                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier.size(150.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Thank you for choosing TiffinApp!",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Your order has been placed successfully.", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Estimated delivery: ~ $estimatedTimeMinutes minutes",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(24.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Button(onClick = onTrackOrderClick) {
                        Text("Track Order")
                    }
                    OutlinedButton(onClick = onPrintReceiptClick) {
                        Text("Print Receipt")
                    }
                }
            }
        }
    }
}



@Composable
fun InsufficientFundsDialog(
    onAddMoneyClick: () -> Unit,
    onChangePaymentMethodClick: () -> Unit,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.4f))
            .clickable(enabled = true, onClick = onDismiss),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Insufficient Funds",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Insufficient Balance",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "You don't have enough money in your wallet to complete this order.",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Please add more money or choose a different payment method.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Button(onClick = onAddMoneyClick) {
                        Text("Add Money")
                    }
                    OutlinedButton(onClick = onChangePaymentMethodClick) {
                        Text("Change Method")
                    }
                }
            }
        }
    }
}


