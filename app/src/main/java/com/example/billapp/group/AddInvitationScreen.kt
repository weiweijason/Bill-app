package com.example.billapp.group

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.billapp.QRCodeScannerActivity
import com.example.billapp.R
import com.example.billapp.viewModel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddInvitationScreen(
    navController: NavController,
    viewModel: MainViewModel
) {
    var groupLink by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    val qrCodeLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val contents = result.data?.getStringExtra("SCAN_RESULT")
            if (contents != null) {
                groupLink = contents
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("加入群組") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        val intent = Intent(navController.context, QRCodeScannerActivity::class.java)
                        qrCodeLauncher.launch(intent)
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_qr_code_scanner_24),
                            contentDescription = "掃描 QR code"
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
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = groupLink,
                onValueChange = {
                    groupLink = it
                    isError = groupLink.isBlank()
                },
                label = { Text("群組連結") },
                modifier = Modifier.fillMaxWidth(),
                isError = isError,
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
            )
            if (isError) {
                Text(
                    text = "群組連結不能為空",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (groupLink.isNotBlank()) {
                        /*TODO: 完成按鈕的邏輯*/
                    } else {
                        isError = true
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = groupLink.isNotBlank()
            ) {
                Text("完成")
            }
        }
    }
}

@Preview
@Composable
fun AddInvitationScreenPreview() {
    // Create a mock NavController
    val navController = rememberNavController()
    // Create a mock or default MainViewModel
    val viewModel = MainViewModel() // You may need to provide required parameters or use a factory if necessary
    AddInvitationScreen(navController = navController, viewModel = viewModel)
}