package com.example.tiffinapp.core.presentation

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tiffinapp.core.data.IngredientRequest
import com.example.tiffinapp.core.data.MealRequest
@Composable
fun UploadScreen(viewModel: MealViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val imageUriState = remember { mutableStateOf<Uri?>(null) }

    var id by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf("") }
    var noOfReviews by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var offer by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var calorie by remember { mutableStateOf("") }
    var dietaryLabel by remember { mutableStateOf("") }
    val uiState by viewModel.uploaduiState.collectAsState()

    var ingredients by remember { mutableStateOf(mutableListOf<IngredientRequest>()) }

    val pickImage = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        imageUriState.value = it
    }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text("Upload Meal", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(value = id, onValueChange = { id = it }, label = { Text("Meal ID") })
        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Meal Name") })
        OutlinedTextField(value = rating, onValueChange = { rating = it }, label = { Text("Rating") })
        OutlinedTextField(value = noOfReviews, onValueChange = { noOfReviews = it }, label = { Text("No of Reviews") })
        OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Price") })
        OutlinedTextField(value = offer, onValueChange = { offer = it }, label = { Text("Offer (%)") })
        OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") })
        OutlinedTextField(value = calorie, onValueChange = { calorie = it }, label = { Text("Calorie") })
        OutlinedTextField(value = dietaryLabel, onValueChange = { dietaryLabel = it }, label = { Text("Dietary Label") })

        Spacer(Modifier.height(16.dp))
        Text("Ingredients", fontWeight = FontWeight.Bold)

        ingredients.forEachIndexed { index, ingredient ->
            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = ingredient.name,
                    onValueChange = {
                        val newList = ingredients.toMutableList()
                        newList[index] = newList[index].copy(name = it)
                        ingredients = newList
                    },
                    label = { Text("Name") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(8.dp))
                OutlinedTextField(
                    value = ingredient.quantity,
                    onValueChange = {
                        val newList = ingredients.toMutableList()
                        newList[index] = newList[index].copy(quantity = it)
                        ingredients = newList
                    },
                    label = { Text("Quantity") },
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = {
                    ingredients = ingredients.toMutableList().also { it.removeAt(index) }
                }) {
                    Icon(Icons.Default.Delete, contentDescription = "Remove")
                }
            }
        }

        Button(onClick = {
            ingredients = ingredients.toMutableList().also {
                it.add(IngredientRequest("", ""))
            }
        }) {
            Text("Add Ingredient")
        }

        Spacer(Modifier.height(16.dp))
        Button(onClick = { pickImage.launch("image/*") }) {
            Text("Select Image")
        }

        imageUriState.value?.let {
            Text("Selected: ${it.lastPathSegment}", fontSize = 12.sp)
        }

        Spacer(Modifier.height(16.dp))
        Button(onClick = {
            val uri = imageUriState.value
            if (uri == null) {
                Toast.makeText(context, "Select image first", Toast.LENGTH_SHORT).show()
                return@Button
            }

            val meal = MealRequest(
                id = id,
                name = name,
                rating = rating.toDoubleOrNull() ?: 0.0,
                noofreviews = noOfReviews.toIntOrNull() ?: 0,
                price = price.toIntOrNull() ?: 0,
                offer = offer.toIntOrNull() ?: 0,
                description = description,
                calorie = calorie,
                dietaryLabel = dietaryLabel,
                ingredients = ingredients
            )

            viewModel.uploadMeal(uri, meal)
        }) {
            Text("Upload Meal")
        }
    }
    when (uiState) {
        is UploadUiState.Loading -> LoadingState("Uploading...")
       //  is LoginUiState.Success -> MessageState((uiState as LoginUiState.Success).message,  Color.Red)
        is UploadUiState.Error -> MessageState((uiState as LoginUiState.Error).message, Color.Red)
        else -> {}
    }
}
