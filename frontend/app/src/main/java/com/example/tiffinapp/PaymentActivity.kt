package com.example.tiffinapp

import dagger.hilt.android.AndroidEntryPoint

// Android Core
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity


import androidx.lifecycle.lifecycleScope
import com.example.tiffinapp.core.domain.WalletRepository
import com.example.tiffinapp.core.util.TokenManager
import com.example.tiffinapp.home.HomeViewModel
import com.example.tiffinapp.wallet.WalletViewModel


import javax.inject.Inject

// Coroutines
import kotlinx.coroutines.launch

// JSON
import org.json.JSONObject

// Razorpay
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener


@AndroidEntryPoint
class PaymentActivity : AppCompatActivity(), PaymentResultListener {

    @Inject lateinit var tokenManager: TokenManager
    @Inject lateinit var repository: WalletRepository
    private val viewModel: WalletViewModel by viewModels()

    private lateinit var razorpay: Checkout
    private var amount: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        amount = intent.getDoubleExtra("amount", 0.0)

        startRazorpayCheckout()
    }

    private fun startRazorpayCheckout() {
        razorpay = Checkout()
        razorpay.setKeyID("Api_Key") // Replace with real key

        val options = JSONObject().apply {
            put("name", "Tiffin Wallet")
            put("description", "Wallet top-up")
            put("currency", "INR")
            put("amount", (amount * 100).toInt()) // Convert to paise
            put("prefill", JSONObject().apply {
                put("email", "test@example.com") // Replace with actual user
                put("contact", "9876543210")
            })
        }

        try {
            razorpay.open(this, options)
        } catch (e: Exception) {
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    override fun onPaymentSuccess(razorpayPaymentId: String?) {
        Toast.makeText(this, "Payment Success: $razorpayPaymentId", Toast.LENGTH_SHORT).show()

        // Call backend to add balance
        lifecycleScope.launch {
            try {
                val token = tokenManager.getToken()
                val response = token?.let { viewModel.addBalanceToServer( amount) }
                if (response!= null) {

                    setResult(RESULT_OK)
                    Log.e("PaymentActivity" ," payment updated for $razorpayPaymentId the response id $response")

                } else {
                    setResult(RESULT_CANCELED)
                }
            } catch (e: Exception) {
                Log.e("PaymentActivity", "Error updating backend: ${e.localizedMessage}")
                setResult(RESULT_CANCELED)
            }
            finish()
        }
    }

    override fun onPaymentError(code: Int, response: String?) {
        Toast.makeText(this, "Payment failed: $response", Toast.LENGTH_LONG).show()
        setResult(RESULT_CANCELED)
        finish()
    }
}
