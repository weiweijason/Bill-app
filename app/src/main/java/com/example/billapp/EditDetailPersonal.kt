package com.example.billapp

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.billapp.models.PersonalTransaction
import com.example.billapp.viewModel.MainViewModel
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDetailScreen(navController: NavController, userId: String, transaction: PersonalTransaction, viewModel: MainViewModel = viewModel()) {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    var date by remember { mutableStateOf(transaction.date?.toDate()?.let { dateFormat.format(it) } ?: "") }
    var amount by remember { mutableStateOf(TextFieldValue(transaction.amount.toString())) }
    var note by remember { mutableStateOf(transaction.note ?: "") }
    var showKeyboard by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("編輯記錄") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
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
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "編輯記錄",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            TextField(
                value = date,
                onValueChange = { date = it },
                label = { Text("日期") },
                modifier = Modifier
                    .fillMaxWidth(),
                readOnly = true // 禁用預設鍵盤
            )
            Button(
                onClick = { showDatePicker = true },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(text = "選擇日期")
            }
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("金額") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        showKeyboard = true
                        keyboardController?.hide() // 隱藏預設鍵盤
                    },
                //readOnly = true // 禁用預設鍵盤
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("備註") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(32.dp))
            if (showKeyboard) {
                CustomKeyboard(
                    onKeyClick = { key ->
                        amount = TextFieldValue(amount.text + key)
                    },
                    onDeleteClick = {
                        if (amount.text.isNotEmpty()) {
                            amount = TextFieldValue(amount.text.dropLast(1))
                        }
                    },
                    onClearClick = {
                        amount = TextFieldValue("")
                    },
                    onOkClick = {
                        showKeyboard = false
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(32.dp))
            }
            Button(
                onClick = {
                    // 保存邏輯
                    val updatedTransaction = transaction.copy(
                        date = Timestamp(dateFormat.parse(date)), // 需要轉換為 Timestamp
                        amount = amount.text.toDoubleOrNull() ?: 0.0,
                        note = note
                    )
                    viewModel.updateTransaction(
                        userId = userId,
                        updatedTransaction = updatedTransaction
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("保存")
            }
        }
    }

    if (showDatePicker) {
        DatePickerModal(
            onDateSelected = { selectedDateMillis ->
                selectedDateMillis?.let {
                    date = convertMillisToDate(it)
                }
                showDatePicker = false
            },
            onDismiss = {
                showDatePicker = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
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
        DatePicker(state = datePickerState)
    }
}

fun convertMillisToDate(millis: Long): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return dateFormat.format(Date(millis))
}