// AddItemScreen.kt
@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.billapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.billapp.ui.theme.BillAppTheme

@Composable
fun AddItemScreen(onAddItem: (String) -> Unit, onBack: () -> Unit) {
    var text by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Group") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
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
            TextField(
                value = text,
                onValueChange = {
                    text = it
                    isError = text.isBlank()
                },
                label = { Text("Group Name") },
                modifier = Modifier.fillMaxWidth(),
                isError = isError,
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
            )
            if (isError) {
                Text(
                    text = "Group name cannot be empty",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (text.isNotBlank()) {
                        onAddItem(text)
                    } else {
                        isError = true
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = text.isNotBlank()
            ) {
                Text("Add")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddItemScreenPreview() {
    BillAppTheme {
        AddItemScreen(onAddItem = {}, onBack = {})
    }
}