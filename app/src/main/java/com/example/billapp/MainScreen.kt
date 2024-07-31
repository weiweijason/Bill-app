// MainScreen.kt
package com.example.myapplication

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.billapp.ui.theme.BillAppTheme
import com.example.billapp.AddItemScreen


@Composable
fun MainScreen() {
    var showAddItemScreen by remember { mutableStateOf(false) }
    var groups by remember { mutableStateOf(listOf<String>()) }

    if (showAddItemScreen) {
        AddItemScreen(
            onAddItem = { item ->
                groups = groups + item // 新增群組
                showAddItemScreen = false
            },
            onBack = { showAddItemScreen = false }
        )
    } else {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                BottomAppBar {
                    Spacer(modifier = Modifier.weight(1f, true))
                    Spacer(modifier = Modifier.weight(1f, true))
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { /* 導向其他頁面 */ },
                    shape = CircleShape
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            },
            floatingActionButtonPosition = FabPosition.End
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "個人",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Left // 改為靠左對齊
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { /* 不跳轉頁面 */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp) // 設定按鈕高度
                        .padding(horizontal = 16.dp)
                ) {
                    Text("個人按鈕")
                }
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "群組",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Left // 改為靠左對齊
                )
                Spacer(modifier = Modifier.height(8.dp))
                groups.forEach { group ->
                    Button(
                        onClick = { /* 導向群組詳細頁面 */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp) // 設定按鈕高度
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(group)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
                Button(
                    onClick = { showAddItemScreen = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp) // 設定按鈕高度
                        .padding(horizontal = 16.dp)
                ) {
                    Text("Add Group")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    BillAppTheme {
        MainScreen()
    }
}