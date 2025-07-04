package com.example.tiffinapp.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import java.util.UUID
import javax.inject.Inject

data class Transaction(
    val id: String,
    val title: String,
    val amount: Double,
    val type: TransactionType,
    val date: Date,
    val status: String = "Completed"
)

enum class TransactionType {
    CREDIT, DEBIT, CASHBACK, REFUND
}

data class WalletFeature(
    val title: String,
    val icon: ImageVector,
    val description: String,
    val onClick: () -> Unit
)

// ViewModel
@HiltViewModel
class WalletViewModel @Inject constructor() : ViewModel() {

    private val _balance = mutableStateOf(2450.75)
    val balance: State<Double> = _balance

    private val _isBalanceVisible = mutableStateOf(true)
    val isBalanceVisible: State<Boolean> = _isBalanceVisible

    private val _transactions = mutableStateOf(getSampleTransactions())
    val transactions: State<List<Transaction>> = _transactions

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    fun toggleBalanceVisibility() {
        _isBalanceVisible.value = !_isBalanceVisible.value
    }

    fun addMoney(amount: Double) {
        _balance.value += amount
        // Add transaction record
        val newTransaction = Transaction(
            id = UUID.randomUUID().toString(),
            title = "Money Added",
            amount = amount,
            type = TransactionType.CREDIT,
            date = Date()
        )
        _transactions.value = listOf(newTransaction) + _transactions.value
    }

    fun refreshBalance() {
        _isLoading.value = true
        // Use viewModelScope instead of GlobalScope
        viewModelScope.launch {
            delay(1000)
            _isLoading.value = false
        }
    }

    private fun getSampleTransactions(): List<Transaction> {
        val calendar = Calendar.getInstance()
        return listOf(
            Transaction("1", "Pizza Palace Order", -285.50, TransactionType.DEBIT, calendar.time),
            Transaction("2", "Cashback Reward", 25.00, TransactionType.CASHBACK, Date(calendar.timeInMillis - 86400000)),
            Transaction("3", "Money Added", 500.00, TransactionType.CREDIT, Date(calendar.timeInMillis - 172800000)),
            Transaction("4", "Burger King Order", -180.25, TransactionType.DEBIT, Date(calendar.timeInMillis - 259200000)),
            Transaction("5", "Refund - Cancelled Order", 95.75, TransactionType.REFUND, Date(calendar.timeInMillis - 345600000))
        )
    }
}