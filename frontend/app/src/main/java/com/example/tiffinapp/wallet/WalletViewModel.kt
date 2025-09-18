package com.example.tiffinapp.wallet

import android.view.SurfaceControl
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiffinapp.core.data.TransactionDto
import com.example.tiffinapp.core.domain.WalletRepository
import com.example.tiffinapp.core.util.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import java.util.UUID
import javax.inject.Inject
import android.util.Log


// ViewModel
@HiltViewModel
class WalletViewModel @Inject constructor(
    private val repository: WalletRepository,
    private val tokenManager: TokenManager // you’ll define this
) : ViewModel() {

    private val _balance = mutableStateOf(0.0)
    val balance: State<Double> = _balance


    private val _isBalanceVisible = mutableStateOf(true)
    val isBalanceVisible: State<Boolean> = _isBalanceVisible

    private val _transactions = mutableStateOf(emptyList<TransactionDto>())
    val transactions: State<List<TransactionDto>> = _transactions

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    fun toggleBalanceVisibility() {
        _isBalanceVisible.value = !_isBalanceVisible.value
    }



    fun fetchWalletData() {
        viewModelScope.launch {
            val token = tokenManager.getToken()
            Log.d("WalletViewModel", "Fetching wallet data with token: $token")

            _isLoading.value = true

            try {
                token?.let {
                    val balance = repository.getBalance(it)
                    Log.d("WalletViewModel", "Fetched balance: $balance")
                    _balance.value = balance

                    val transactions = repository.getTransactions(it)
                    Log.d("WalletViewModel", "Fetched ${transactions.size} transactions")
                    _transactions.value = transactions
                } ?: run {
                    Log.e("WalletViewModel", "Token is null, cannot fetch data")
                }
            } catch (e: Exception) {
                Log.e("WalletViewModel", "Error fetching wallet data: ${e.localizedMessage}", e)
            } finally {
                _isLoading.value = false
                Log.d("WalletViewModel", "Wallet data fetch complete")
            }
        }
    }




    public fun addBalanceToServer(amount: Double) {
        viewModelScope.launch {

            try {
                val token = tokenManager.getToken()
                val response = token?.let { repository.addBalance(it, amount) }
                if (response != null) {
                    if (response.status) {
                        fetchWalletData()
                    }
                }
            } catch (e: Exception) {
                // handle
            }
        }
    }
}
