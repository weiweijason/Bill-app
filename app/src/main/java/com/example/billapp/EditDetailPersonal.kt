package com.example.billapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.billapp.viewModel.MainViewModel
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTransactionDetailScreen(
    navController: NavController,
    transactionId: String,
    viewModel: MainViewModel = viewModel() // Use viewModel() to obtain the ViewModel
) {
    // Fetch transaction data when the screen is first composed
    LaunchedEffect(transactionId) {
        viewModel.getTransaction(transactionId)
    }

    // State to hold the transaction data
    val transaction by viewModel.transaction.collectAsState()

    // UI states for editing
    val dateFormat = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }
    val (date, setDate) = remember { mutableStateOf("") }
    val (amount, setAmount) = remember { mutableStateOf("") }
    val (note, setNote) = remember { mutableStateOf("") }
    var showKeyboard by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    // Set initial values into the ViewModel
    LaunchedEffect(transaction) {
        transaction?.let {
            setDate(dateFormat.format(it.date!!.toDate()))
            setAmount(it.amount.toString())
            setNote(it.note ?: "")
        }
    }

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
            // Date Field
            TextField(
                value = date,
                onValueChange = { },
                label = { Text("Date") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true
            )
            Button(
                onClick = { showDatePicker = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Select Date")
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Amount Field
            TextField(
                value = amount,
                onValueChange = { setAmount(it) },
                label = { Text("Amount") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Note Field
            TextField(
                value = note,
                onValueChange = { setNote(it) },
                label = { Text("Note") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(32.dp))

            // Custom Keyboard
            if (showKeyboard) {
                CustomKeyboard(
                    onKeyClick = { key ->
                        setAmount(amount + key)
                    },
                    onDeleteClick = {
                        if (amount.isNotEmpty()) {
                            setAmount(amount.dropLast(1))
                        }
                    },
                    onClearClick = {
                        setAmount("")
                    },
                    onOkClick = {
                        showKeyboard = false
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(32.dp))
            }

            // Save Button
            Button(
                onClick = {
                    transaction?.let {
                        val updatedTransaction = it.copy(
                            date = Timestamp(dateFormat.parse(date)), // Convert date to Timestamp
                            amount = amount.toDoubleOrNull() ?: 0.0,
                            note = note
                        )
                        viewModel.updateTransaction(
                            transactionId = it.transactionId,
                            updatedTransaction = updatedTransaction
                        )
                        navController.navigateUp() // Navigate back after saving
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save")
            }
        }
    }

    // Date Picker
    if (showDatePicker) {
        DatePickerModal(
            onDateSelected = { selectedDateMillis ->
                selectedDateMillis?.let {
                    setDate(convertMillisToDate(it))
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
