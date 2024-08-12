package com.example.billapp

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.billapp.ui.theme.BillAppTheme

data class User(
    val id: String,
    val name: String,
    val avatarUrl: String
)

data class Transaction(
    val from: User,
    val to: User,
    val amount: Double
)

data class Group(
    val name: String,
    val transactions: List<Transaction>
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(modifier: Modifier) {
    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf("首頁", "個人", "新增", "群組", "設定")
    val icons = listOf(
        R.drawable.baseline_home_24,
        R.drawable.baseline_person_24,
        R.drawable.baseline_add_24,
        R.drawable.baseline_groups_24,
        R.drawable.baseline_settings_24
    )
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                painter = painterResource(id = icons[index]),
                                contentDescription = item
                            ) },
                        label = { Text(item) },
                        selected = selectedItem == index,
                        onClick = { selectedItem = index }
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedItem) {
                0 -> HomeScreen()
                1 -> PlaceholderScreen("個人")
                2 -> PlaceholderScreen("新增")
                3 -> GroupScreen()
                4 -> SettingScreen()
            }
        }
    }
}

@Composable
fun PlaceholderScreen(title: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    BillAppTheme {
        MainScreen(Modifier)
    }
}