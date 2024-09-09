package com.example.billapp.bonus

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import java.util.concurrent.TimeUnit
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangeRateTableScreen(navController: NavController, baseCurrency: String, currencies: List<String>) {
    var exchangeRates by remember { mutableStateOf<Map<String, Double>?>(null) }
    var lastUpdateTime by remember { mutableStateOf<Long?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(baseCurrency) {
        coroutineScope.launch {
            val lastUpdate = DataStoreManager.getLastUpdateTime(context).first()
            lastUpdateTime = lastUpdate
            val currentTime = System.currentTimeMillis()
            if (currentTime - (lastUpdate ?: 0L) > TimeUnit.DAYS.toMillis(1)) {
                try {
                    val response = RetrofitInstance.api.getLatestRates("4d3d42c12ac2d28c5f08058b", baseCurrency)
                    val rates = response.conversion_rates.filterKeys { it in currencies }
                    exchangeRates = rates
                    rates.forEach { (currency, rate) ->
                        DataStoreManager.saveExchangeRate(context, currency, rate)
                    }
                    DataStoreManager.saveLastUpdateTime(context, currentTime)
                    lastUpdateTime = currentTime
                } catch (e: Exception) {
                    exchangeRates = null
                }
            } else {
                val rates = mutableMapOf<String, Double>()
                currencies.forEach { currency ->
                    val rate = DataStoreManager.getExchangeRate(context, currency).first()
                    if (rate != null) {
                        rates[currency] = rate
                    }
                }
                exchangeRates = rates
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "匯率表格") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "TWD 對其他幣值的匯率", style = MaterialTheme.typography.h5)

            Spacer(modifier = Modifier.height(16.dp))

            exchangeRates?.let { rates ->
                rates.forEach { (currency, rate) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = getCurrencyName(currency), modifier = Modifier.weight(1f))
                        Text(text = String.format("%.4f", 1 / rate), modifier = Modifier.weight(1f), textAlign = TextAlign.End)
                    }
                }
            } ?: run {
                Text(text = "正在載入匯率...")
            }

            Spacer(modifier = Modifier.height(16.dp))

            lastUpdateTime?.let {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val date = Date(it)
                Text(
                    text = "資料抓取時間: ${dateFormat.format(date)}",
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

fun getCurrencyName(currencyCode: String): String {
    return when (currencyCode) {
        "TWD" -> "新台幣"
        "USD" -> "美元"
        "EUR" -> "歐元"
        "JPY" -> "日圓"
        "GBP" -> "英鎊"
        "AUD" -> "澳幣"
        "CAD" -> "加幣"
        "CHF" -> "瑞士法郎"
        "CNY" -> "人民幣"
        "SEK" -> "瑞典克朗"
        "NZD" -> "紐西蘭元"
        else -> currencyCode
    }
}