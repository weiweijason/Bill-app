package com.example.billapp.personal

import android.net.Uri
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.billapp.PieChart
import com.example.billapp.PieChartWithCategory
import com.example.billapp.viewModel.MainViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PersonalUIScreen(
    navController: NavController,
    viewModel: MainViewModel
) {
    val user by viewModel.user.collectAsState()
    var year by remember { mutableStateOf(Calendar.getInstance().get(Calendar.YEAR)) }
    var month by remember { mutableStateOf(Calendar.getInstance().get(Calendar.MONTH) + 1) }
    var day by remember { mutableStateOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) }
    val transactions by viewModel.userTransactions.collectAsState()
    var selectedChart by remember { mutableStateOf("結餘") }
    var filteredRecords by remember { mutableStateOf(transactions) }
    var Type by remember { mutableStateOf("balance") }
    var dateType by remember { mutableStateOf("月") }
    var filteredIncome by remember { mutableStateOf(0f) }
    var filteredExpense by remember { mutableStateOf(0f) }
    var filteredBalance by remember { mutableStateOf(0f) }
    var showDatePicker by remember { mutableStateOf(false) }

    // 根據選中的類型過濾記錄
    fun filterRecords() {
        val filtered = transactions.filter { transaction ->
            val calendar = Calendar.getInstance().apply { timeInMillis = transaction.date!!.toDate().time }
            when (dateType) {
                "年" -> calendar.get(Calendar.YEAR) == year
                "月" -> calendar.get(Calendar.YEAR) == year && calendar.get(Calendar.MONTH) + 1 == month
                "日" -> calendar.get(Calendar.YEAR) == year && calendar.get(Calendar.MONTH) + 1 == month && calendar.get(Calendar.DAY_OF_MONTH) == day
                else -> true
            }
        }
        filteredRecords = filtered.filter { transaction ->
            when (selectedChart) {
                "支出" -> transaction.type == "支出"
                "收入" -> transaction.type == "收入"
                "結餘" -> true
                else -> true
            }
        }

        // 計算篩選後的收入、支出和結餘
        filteredIncome = filtered.filter { it.type == "收入" }.sumOf { it.amount }.toFloat()
        filteredExpense = filtered.filter { it.type == "支出" }.sumOf { it.amount }.toFloat()
        filteredBalance = filteredIncome - filteredExpense
    }

    LaunchedEffect(user) {
        user?.let {
            viewModel.loadUserTransactions()
            filterRecords()
        }
    }
    LaunchedEffect(transactions) {
        filterRecords()
    }

    // 更新日期
    fun updateDate(increment: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, day)
        when (dateType) {
            "年" -> calendar.add(Calendar.YEAR, increment)
            "月" -> calendar.add(Calendar.MONTH, increment)
            "日" -> calendar.add(Calendar.DAY_OF_MONTH, increment)
        }
        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH) + 1
        day = calendar.get(Calendar.DAY_OF_MONTH)

        // 更新數據
        filterRecords()  // 過濾記錄
    }

    // 切換數據類型（支出、收入或結餘）
    fun switchData(type: String) {
        selectedChart = type
        filterRecords()
    }

    // 獲取顯示金額
    fun getDisplayAmount(): Float {
        return viewModel.getUserAmount()
    }

    // 格式化日期
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 顯示年、月、日的 Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .border(1.dp, Color.Gray)
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { dateType = "年"; filterRecords() },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "年", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(text = "$year", fontSize = 20.sp, fontWeight = FontWeight.Medium)
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { dateType = "月"; filterRecords() },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "月", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(text = "$month", fontSize = 20.sp, fontWeight = FontWeight.Medium)
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { dateType = "日"; filterRecords() },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "日", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(text = "$day", fontSize = 20.sp, fontWeight = FontWeight.Medium)
            }
        }

        // 顯示年月的 Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { updateDate(-1) }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "上一個"
                )
            }

            Box(
                modifier = Modifier
                    .clickable { showDatePicker = true }
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = when (dateType) {
                        "年" -> "$year"
                        "月" -> "$year/$month"
                        "日" -> "$year/$month/$day"
                        else -> "$year/$month/"
                    },
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            IconButton(onClick = { updateDate(1) }) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "下一個"
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 顯示已支出、結餘、收入的 Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .border(1.dp, Color.Gray)
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { switchData("支出"); Type = "expanse" },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "支出", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(text = "$filteredExpense", fontSize = 20.sp, fontWeight = FontWeight.Medium)
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { switchData("結餘"); Type = "balance" },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "結餘", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(text = "$filteredBalance", fontSize = 20.sp, fontWeight = FontWeight.Medium)
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { switchData("收入"); Type = "income" },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "收入", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(text = "$filteredIncome", fontSize = 20.sp, fontWeight = FontWeight.Medium)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .align(Alignment.CenterHorizontally)
        ) {
            // PieChart
            val income = filteredIncome
            val expense = filteredExpense
            val total = filteredIncome + filteredExpense
            val balance = filteredBalance
            PieChartWithCategory(income = income, expense = expense, balance = balance, total = total, selectedCategory = Type)
        }

        Spacer(modifier = Modifier.height(32.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Text(
                text = "收入和支出詳情",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            PersonalTransactionList(
                transactions = filteredRecords,
                navController = navController
            )
        }
    }
    if (showDatePicker) {
        PickerModal(
            onDateSelected = { selectedDate ->
                // 根據 dateType 更新 year, month, day
                val calendar = Calendar.getInstance().apply { timeInMillis = selectedDate ?: 0L }
                when (dateType) {
                    "年" -> year = calendar.get(Calendar.YEAR)
                    "月" -> {
                        year = calendar.get(Calendar.YEAR)
                        month = calendar.get(Calendar.MONTH) + 1
                    }
                    "日" -> {
                        year = calendar.get(Calendar.YEAR)
                        month = calendar.get(Calendar.MONTH) + 1
                        day = calendar.get(Calendar.DAY_OF_MONTH)
                    }
                }
                filterRecords()
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false },
            dateType = dateType
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PickerModal(
    dateType: String,
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(
            state = datePickerState,
            mode = when (dateType) {
                "年" -> DatePickerMode.YEAR
                "月" -> DatePickerMode.YEAR_MONTH
                "日" -> DatePickerMode.DEFAULT
                else -> DatePickerMode.DEFAULT
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PersonalUIScreenPreview() {
    val navController = rememberNavController()
    val viewModel = MainViewModel()
    PersonalUIScreen(navController, viewModel)
}