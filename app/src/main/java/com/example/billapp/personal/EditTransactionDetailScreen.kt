package com.example.billapp.personal

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.billapp.CustomKeyboard
import com.example.billapp.R
import com.example.billapp.StylishTextField
import com.example.billapp.evaluateExpression
import com.example.billapp.models.PersonalTransaction
import com.example.billapp.models.TransactionCategory
import com.example.billapp.viewModel.MainViewModel
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTransactionDetailScreen(
    navController: NavController,
    transactionId: String,
    viewModel: MainViewModel = viewModel()
) {
    // Observe the transaction data from the ViewModel
    val transaction by viewModel.transaction.collectAsState()
    val note by viewModel.note.collectAsState()
    val amount by viewModel.amount.collectAsState()
    var amountInput by remember { mutableStateOf(amount.toString()) }
    val name by viewModel.name.collectAsState()
    val date by viewModel.date.collectAsState()
    var currentTimestamp by remember { mutableStateOf(Timestamp.now()) }
    val type by viewModel.transactionType.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    val selectedCategory by viewModel.category.collectAsState()
    val categories = TransactionCategory.entries.toTypedArray()
    val focusManager = LocalFocusManager.current

    var isBottomSheetVisible by remember { mutableStateOf(false) }
    var toggleKeyboard by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    // Fetch transaction data when the screen is first composed
    LaunchedEffect(transactionId) {
        viewModel.getTransaction(transactionId)
    }

    LaunchedEffect(amount) {
        // 初始化時根據amount是否為整數決定顯示的內容
        amountInput = if (amount % 1.0 == 0.0) {
            amount.toInt().toString()
        } else {
            amount.toString()
        }
    }

    // Date formatting
    val dateFormat = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }

    var showDatePicker by remember { mutableStateOf(false) }

    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Transaction") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Type Selection
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { viewModel.setTransactionType("收入") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (type == "收入") Color.Gray else Color.LightGray
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "收入", color = Color.Black)
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = { viewModel.setTransactionType("支出") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor =  if(type == "支出") Color.Gray else Color.LightGray
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "支出", color = Color.Black)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Date Field
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                TextField(
                    value = date.toDate().let { dateFormat.format(it) } ?: "",
                    onValueChange = { },
                    label = { Text("Date") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable { showDatePicker = true }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
/*
            // Amount Field
            TextField(
                value = amountInput,
                onValueChange = {
                    amountInput = it
                    it.toDoubleOrNull()?.let { validAmount ->
                        viewModel.setAmount(validAmount)
                        // 根據是否為整數來決定顯示的內容
                        amountInput = if (validAmount % 1.0 == 0.0) {
                            validAmount.toInt().toString()
                        } else {
                            validAmount.toString()
                        }
                    }
                },
                label = { Text("Amount") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
*/
            // 金額輸入
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                TextField(
                    value = amountInput,
                    onValueChange = {
                        amountInput = it
                        it.toDoubleOrNull()?.let { validAmount ->
                            viewModel.setAmount(validAmount)
                            // 根據是否為整數來決定顯示的內容
                            amountInput = if (validAmount % 1.0 == 0.0) {
                                validAmount.toInt().toString()
                            } else {
                                validAmount.toString()
                            }
                        }
                    },
                    label = { Text("金額") },
                    modifier = Modifier.fillMaxWidth(),
//                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable {
                            toggleKeyboard = !toggleKeyboard
                            isBottomSheetVisible = toggleKeyboard
                        }
                )
            }

            AnimatedVisibility(visible = isBottomSheetVisible) {
                CustomKeyboard(
                    onKeyClick = { key ->
                        amountInput += key
                        // 驗證並更新金額輸入
                        amountInput.toDoubleOrNull()?.let { validAmount ->
                            viewModel.setAmount(validAmount)
                        }
                    },
                    onDeleteClick = {
                        if (amountInput.isNotEmpty()) {
                            amountInput = amountInput.dropLast(1)
                            amountInput.toDoubleOrNull()?.let { validAmount ->
                                viewModel.setAmount(validAmount)
                            }
                        }
                    },
                    onClearClick = {
                        amountInput = ""
                        viewModel.setAmount(0.0)
                    },
                    onOkClick = {
                        coroutineScope.launch {
                            isBottomSheetVisible = false
                            toggleKeyboard = false
                        }
                    },
                    onEqualsClick = {
                        // 計算 amountInput
                        val result = evaluateExpression(amountInput)
                        amountInput = result.toString()
                        viewModel.setAmount(result)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
            }


            Spacer(modifier = Modifier.height(16.dp))

//            Text(text = "type: $type")

            // Note Field
            TextField(
                value = note,
                onValueChange = { newNote ->
                    viewModel.setNote(newNote)
                },
                label = { Text("Note") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Name Field
            TextField(
                value = name,
                onValueChange = { newName ->
                    viewModel.setName(newName)
                },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
//            Text(text = "當前時間戳記: $currentTimestamp")



            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                TextField(
                    value = selectedCategory,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("選擇類別") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    modifier = Modifier.exposedDropdownSize(matchTextFieldWidth = true),
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category.name) },
                            onClick = {
                                viewModel.setCategory(category)
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            // Save Button
            Button(
                onClick = {
                    val amountValue = amountInput.toDoubleOrNull() ?: 0.0
                    if (amountInput.isNotBlank() && name.isNotBlank() && amountValue != 0.0) {
                        currentTimestamp = Timestamp.now()
                        viewModel.setUpdatetime(currentTimestamp)
                        transaction?.let {
                            viewModel.updateTransaction(
                                transactionId = it.transactionId,
                                updatedTransaction = PersonalTransaction(
                                    name = viewModel.name.value,
                                    transactionId = it.transactionId,
                                    date = viewModel.date.value,
                                    amount = viewModel.amount.value,
                                    note = viewModel.note.value,
                                    updatedAt = viewModel.updatetime.value,
                                    type = viewModel.transactionType.value,
                                    category = viewModel.stringToCategory(selectedCategory)
                                )
                            )
                            viewModel.loadUserTransactions()
                            navController.navigateUp()
                        }
                    } else {
                        // Show error message or handle empty fields
                        errorMessage = "金額和名稱欄位不得留空"
                        showErrorDialog = true
                    }
                },
                enabled = amountInput.isNotBlank() && name.isNotBlank() && amountInput.toDoubleOrNull() != 0.0,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save")
            }

            //Delete Button
            DeleteTransactionButton(transaction = transaction, viewModel = viewModel, navController = navController)
        }
    }

    // Date Picker
    if (showDatePicker) {
        DatePickerModal(
            onDateSelected = { selectedDateMillis ->
                selectedDateMillis?.let {
                    viewModel.setDate(Timestamp(Date(it)))
                }
                showDatePicker = false
            },
            onDismiss = {
                showDatePicker = false
            }
        )
    }

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text(text = "錯誤") },
            text = { Text(text = errorMessage) },
            confirmButton = {
                Button(
                    onClick = { showErrorDialog = false }
                ) {
                    Text("確定")
                }
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

@Composable
fun DeleteTransactionButton(
    transaction: PersonalTransaction?,
    viewModel: MainViewModel,
    navController: NavController
) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "確認刪除") },
            text = { Text(text = "你確定要刪除此交易紀錄嗎？") },
            confirmButton = {
                Button(
                    onClick = {
                        transaction?.let {
                            viewModel.deleteTransaction(it.transactionId, it.type, it.amount)
                            navController.navigateUp()
                        }
                        showDialog = false
                    }
                ) {
                    Text("確定")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog = false }
                ) {
                    Text("取消")
                }
            }
        )
    }

    Button(
        onClick = { showDialog = true },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Red,
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_delete),
            contentDescription = "Delete Group"
        )
        Spacer(modifier = Modifier.width(4.dp)) // Reduced space between icon and text
        Text(text = "刪除交易")
    }
}
