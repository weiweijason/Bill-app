// MainScreen.kt
package com.example.billapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.billapp.ui.theme.BillAppTheme

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
                    FloatingActionButton(
                        onClick = { /* 新增新的一筆帳目邏輯 */ },
                        shape = CircleShape
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add")
                    }
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Text(
                    text = "個人",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textAlign = TextAlign.Left,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp) // 占據頁面一半的位置
                        .padding(horizontal = 16.dp)
                        .clickable(onClick = {/*跳轉至個人頁面邏輯*/}),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "餘額: $0",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "群組",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textAlign = TextAlign.Left,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    items(groups) { group ->
                        Button(
                            onClick = { /* 導向群組詳細頁面 */ },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp) // 設定按鈕高度
                                .padding(vertical = 4.dp)
                        ) {
                            Text(group)
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { showAddItemScreen = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp) // 設定按鈕高度
                                .padding(vertical = 4.dp)
                        ) {
                            Text("新增群組")
                        }
                    }
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