package com.example.tiffinapp.wallet

import android.app.Activity
import android.content.Intent
import android.view.SurfaceControl
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tiffinapp.PaymentActivity
import com.example.tiffinapp.core.data.TransactionDto
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun WalletScreen(
    viewModel: WalletViewModel = hiltViewModel()
) {
    val balance by viewModel.balance
    val isBalanceVisible by viewModel.isBalanceVisible
    val transactions by viewModel.transactions
    val isLoading by viewModel.isLoading
    val context = LocalContext.current

    val activityLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.fetchWalletData()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.fetchWalletData()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
            Text("My Order", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            }
            Column {  IconButton(onClick = { /* Handle notifications */ }) {


                   Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "Notifications"
                )
                   Spacer( modifier = Modifier.width(8.dp))
            }}
        }
       // WalletTopBar()

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                BalanceCard(
                    balance = balance,
                    isVisible = isBalanceVisible,
                    isLoading = isLoading,
                    onToggleVisibility = { viewModel.toggleBalanceVisibility() },
                    onRefresh = { viewModel.fetchWalletData() }
                )
            }

            item {
                QuickActionsSection(
                    onAddMoney = { amount ->

                            val intent = Intent(context, PaymentActivity::class.java)
                            intent.putExtra("amount", amount)
                            activityLauncher.launch(intent)

                    }
                )
            }

            item {
                FeaturesGrid()
            }

            item {
                RecentTransactionsSection(transactions = transactions)
            }
        }
    }
}

// The rest of the original UI code remains unchanged...


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletTopBar() {
    TopAppBar(
        title = {
            Text(
                text = "My Wallet",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        },
        actions = {
            IconButton(onClick = { /* Handle notifications */ }) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "Notifications"
                )
            }
            IconButton(onClick = { /* Handle profile */ }) {
                Icon(
                    imageVector = Icons.Outlined.AccountCircle,
                    contentDescription = "Profile"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

@Composable
fun BalanceCard(
    balance: Double,
    isVisible: Boolean,
    isLoading: Boolean,
    onToggleVisibility: () -> Unit,
    onRefresh: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFFFF6B35),
                            Color(0xFFFFB74D)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Available Balance",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Row {
                        IconButton(
                            onClick = onToggleVisibility,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = if (isVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                contentDescription = if (isVisible) "Hide balance" else "Show balance",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(
                            onClick = onRefresh,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Refresh,
                                contentDescription = "Refresh balance",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                } else {
                    Text(
                        text = if (isVisible) "₹${String.format("%.2f", balance)}" else "₹••••••",
                        color = Color.White,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Last updated: ${SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault()).format(Date())}",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 12.sp
                    )

                    Icon(
                        imageVector = Icons.Filled.AccountBalanceWallet,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.3f),
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}





@Composable
fun QuickActionsSection(onAddMoney: (Double) -> Unit) {
    val quickAmounts = listOf(100.0, 200.0, 500.0, 1000.0)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Quick Add Money",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(quickAmounts) { amount ->
                    AssistChip(
                        onClick = { onAddMoney(amount) },
                        label = {
                            Text(
                                text = "₹${amount.toInt()}",
                                fontWeight = FontWeight.Medium
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            labelColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { onAddMoney(102.3) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Add Custom Amount",
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun FeaturesGrid() {
    val features = listOf(
        WalletFeature("Payment Settings", Icons.Outlined.Settings, "Manage payment methods") {},
        WalletFeature("Refer & Earn", Icons.Outlined.Share, "Invite friends and earn") {},
        WalletFeature("Offers", Icons.Outlined.LocalOffer, "Check latest offers") {},
        WalletFeature("Get Help", Icons.Outlined.Help, "Support & FAQ") {},
        WalletFeature("Security", Icons.Outlined.Security, "Secure your wallet") {},
        WalletFeature("Rewards", Icons.Outlined.Stars, "View reward points") {}
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Wallet Features",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            for (i in features.indices step 2) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    FeatureItem(features[i], Modifier.weight(1f))
                    if (i + 1 < features.size) FeatureItem(features[i + 1], Modifier.weight(1f))
                    else Spacer(modifier = Modifier.weight(1f))
                }
                if (i + 2 < features.size) Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun FeatureItem(feature: WalletFeature, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .aspectRatio(1f)
            .clickable { feature.onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = feature.icon,
                contentDescription = feature.title,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = feature.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = feature.description,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 14.sp
            )
        }
    }
}

@Composable
fun RecentTransactionsSection(transactions: List<TransactionDto>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recent Transactions",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                TextButton(onClick = { /* Handle view all */ }) {
                    Text(
                        text = "View All",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            transactions.take(5).forEach { transaction ->
                TransactionItem(transaction = transaction)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}


@Composable
fun TransactionItem(transaction: TransactionDto) {
    val parsedDate = remember(transaction.timestamp) {
        try {
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).parse(transaction.timestamp)
        } catch (e: Exception) {
            null
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        when (transaction.type) {
                            TransactionType.CREDIT.name -> MaterialTheme.colorScheme.tertiaryContainer
                            TransactionType.DEBIT.name -> MaterialTheme.colorScheme.errorContainer
                            else -> MaterialTheme.colorScheme.secondaryContainer
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (transaction.type) {
                        TransactionType.CREDIT.name -> Icons.Filled.Add
                        TransactionType.DEBIT.name -> Icons.Filled.Remove
                        TransactionType.CASHBACK.name -> Icons.Filled.Stars
                        TransactionType.REFUND.name -> Icons.Filled.Refresh
                        else -> Icons.Filled.Info
                    },
                    contentDescription = null,
                    tint = when (transaction.type) {
                        TransactionType.CREDIT.name -> MaterialTheme.colorScheme.tertiary
                        TransactionType.DEBIT.name -> MaterialTheme.colorScheme.error
                        TransactionType.CASHBACK.name -> MaterialTheme.colorScheme.secondary
                        TransactionType.REFUND.name -> MaterialTheme.colorScheme.primary
                        else -> MaterialTheme.colorScheme.outline
                    },
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = "Txn #${transaction.id}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = parsedDate?.let {
                        SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault()).format(it)
                    } ?: "Invalid Date",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Text(
            text = "${if (transaction.type == TransactionType.CREDIT.name || transaction.type == TransactionType.CASHBACK.name || transaction.type == TransactionType.REFUND.name) "+" else "-"}₹${String.format("%.2f", kotlin.math.abs(transaction.amount))}",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (transaction.type == TransactionType.CREDIT.name || transaction.type == TransactionType.CASHBACK.name || transaction.type == TransactionType.REFUND.name) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.error
        )
    }
}


enum class TransactionType {
    CREDIT, DEBIT, CASHBACK, REFUND
}

data class WalletFeature(
    val title: String,
    val icon: ImageVector,
    val description: String,
    val onClick: () -> Unit
)