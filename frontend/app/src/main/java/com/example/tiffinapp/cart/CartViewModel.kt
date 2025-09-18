package com.example.tiffinapp.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiffinapp.core.data.CartItemDto
import com.example.tiffinapp.core.data.OrderRequestDto
import com.example.tiffinapp.core.data.UserDetailDto
import com.example.tiffinapp.core.data.UserDetailFullDto
import com.example.tiffinapp.core.domain.CartRepository
import com.example.tiffinapp.core.domain.WalletRepository
import com.example.tiffinapp.core.util.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val repository: CartRepository,
    private val tokenManager: TokenManager,
    private val wallletRepository: WalletRepository
) : ViewModel() {

    private val _cartItems = MutableLiveData<List<CartItemDto>>()
    val cartItems: LiveData<List<CartItemDto>> = _cartItems

    private val _userDetail = MutableLiveData<UserDetailDto?>()
    val userDetail: MutableLiveData<UserDetailDto?> = _userDetail
    private val _userDetailfull = MutableLiveData<UserDetailFullDto?>()
    val userDetailfull: MutableLiveData<UserDetailFullDto?> = _userDetailfull

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun fetchCart() {
        viewModelScope.launch {
            val token = tokenManager.getToken()
            token?.let {
                val cart = repository.getCart(it)
                _cartItems.value = cart
            }
        }
    }
    fun addItem( mealId: String, quantity: Int) {
        viewModelScope.launch {
            try {
                val token = tokenManager.getToken()
                token?.let { repository.addToCart(it, mealId, quantity) }
                fetchCart()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun removeItem( mealId: String) {
        viewModelScope.launch {
            try {
                val token = tokenManager.getToken()
                if (token != null) {
                    repository.removeFromCart(token, mealId)
                }
                fetchCart()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            try {
                val token = tokenManager.getToken()
                if (token != null) {
                    repository.clearCart(token)
                }
                fetchCart()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }



    fun fetchUserDetail() {
        viewModelScope.launch {
            try {
                val token = tokenManager.getToken()
                token?.let {
                    _userDetailfull.value = repository.getUserFullDetail(it)
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun updateUserDetail(phone: String, address: String) {
        viewModelScope.launch {
            try {
                val token = tokenManager.getToken()
                token?.let {
                    _userDetail.value = repository.updateUserDetail(it, phone, address)
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun createUserDetail(phone: String, address: String) {
        viewModelScope.launch {
            try {
                val token = tokenManager.getToken()
                token?.let {
                    _userDetail.value = repository.createUserDetail(it, phone, address)
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun deleteUserDetail() {
        viewModelScope.launch {
            try {
                val token = tokenManager.getToken()
                token?.let {
                    repository.deleteUserDetail(it)
                    _userDetail.value = null
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun placeOrder(
        listOfMeals: String,
        totalCost: Double,
        transactionType: String,
        transactionId: String,
        name: String,
        address: String,
        mobileNo: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val token = tokenManager.getToken()
                token?.let {
                    repository.placeOrder(
                        token = it,
                        orderRequest = OrderRequestDto(
                            listOfMeals = listOfMeals,
                            totalCost = totalCost,
                            transactionType = transactionType,
                            transactionId = transactionId,
                            name = name,
                            address = address,
                            mobileNo = mobileNo
                        )
                    )
                    onSuccess()
                }
            } catch (e: Exception) {
                onFailure(e.message ?: "Unknown error")
            }
        }
    }

    fun addBalanceToServer(amount: Double, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val token = tokenManager.getToken()
                val response = token?.let { wallletRepository.addBalance(it, amount) }

                if (response != null) {
                    onResult(response.status) // ✅ Return true or false based on status
                } else {
                    onResult(false) // null response
                }

            } catch (e: Exception) {
                onResult(false) // error occurred
            }
        }
    }




}
