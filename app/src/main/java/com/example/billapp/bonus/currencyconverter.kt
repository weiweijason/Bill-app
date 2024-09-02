package com.example.billapp.bonus

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

data class ExchangeRateResponse(
    val conversion_rates: Map<String, Double>
)

interface ExchangeRateApiService {
    @GET("v6/{apiKey}/latest/{base}")
    suspend fun getLatestRates(
        @Path("apiKey") apiKey: String,
        @Path("base") base: String
    ): ExchangeRateResponse
}

object RetrofitInstance {
    private const val BASE_URL = "https://v6.exchangerate-api.com/"

    val api: ExchangeRateApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ExchangeRateApiService::class.java)
    }
}

suspend fun convertCurrency(amount: Double, fromCurrency: String, toCurrency: String): String {
    return withContext(Dispatchers.IO) {
        try {
            val response = RetrofitInstance.api.getLatestRates("4d3d42c12ac2d28c5f08058b", fromCurrency)
            val fromRate = response.conversion_rates[fromCurrency] ?: 1.0
            val toRate = response.conversion_rates[toCurrency] ?: 1.0
            val convertedAmount = amount * (toRate / fromRate)
            String.format("%.2f", convertedAmount)
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }
}

@Composable
fun CurrencyConverterScreen(navController: NavController) {
    var amount by remember { mutableStateOf("") }
    var fromCurrency by remember { mutableStateOf("TWD") }
    var toCurrency by remember { mutableStateOf("EUR") }
    var result by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    val currencies = listOf("TWD", "USD", "EUR", "JPY", "GBP", "AUD", "CAD", "CHF", "CNY", "SEK", "NZD")

    Scaffold (
        topBar = {
            TopAppBar(
                title = {Text(text = "貨幣轉換器") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }
    ){ innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "貨幣轉換器", style = MaterialTheme.typography.h4)

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("金額") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { focusManager.clearFocus() }
            )

            Spacer(modifier = Modifier.height(16.dp))

            ExposedDropdownMenu(
                label = "從",
                selectedCurrency = fromCurrency,
                onCurrencySelected = { fromCurrency = it },
                currencies = currencies
            )

            Spacer(modifier = Modifier.height(16.dp))

            ExposedDropdownMenu(
                label = "到",
                selectedCurrency = toCurrency,
                onCurrencySelected = { toCurrency = it },
                currencies = currencies
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                coroutineScope.launch {
                    result =
                        convertCurrency(amount.toDoubleOrNull() ?: 0.0, fromCurrency, toCurrency)
                }
            }) {
                Text("轉換")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "結果: $result",
                style = MaterialTheme.typography.h5.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primary
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                navController.navigate("exchangeRateTable")
            }) {
                Text("顯示匯率表格")
            }
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ExposedDropdownMenu(
    label: String,
    selectedCurrency: String,
    onCurrencySelected: (String) -> Unit,
    currencies: List<String>
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(TextFieldValue(selectedCurrency)) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedText,
            onValueChange = { selectedText = it },
            label = { Text(label) },
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            currencies.forEach { currency ->
                DropdownMenuItem(onClick = {
                    selectedText = TextFieldValue(currency)
                    onCurrencySelected(currency)
                    expanded = false
                }) {
                    Text(text = currency)
                }
            }
        }
    }
}

