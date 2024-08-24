package com.example.billapp

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.billapp.group.GroupList
import com.example.billapp.models.User
import com.example.billapp.models.GroupTransaction
import com.example.billapp.viewModel.MainViewModel

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    onOpenDrawer: () -> Unit,
    viewModel: MainViewModel
) {
    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf("首頁", "個人", "新增", "群組", "設定")
    val icons = listOf(
        R.drawable.baseline_home_24,
        R.drawable.baseline_person_24,
        R.drawable.baseline_add_24,
        R.drawable.baseline_groups_24,
        R.drawable.baseline_settings_24
    )

    var showAddItemScreen by remember { mutableStateOf(false) }

    val groups by viewModel.userGroups.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My App") },
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menu")
                    }
                }
            )
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

            val pagerState = rememberPagerState(pageCount = { 2 })
            val coroutineScope = rememberCoroutineScope()

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(horizontal = 16.dp)
            ) { page ->
                when (page) {
                    0 -> Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable {
                                // 跳轉至個人頁面邏輯
                                navController.navigate("personal")
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        val amount = viewModel.getUserAmount()
                        Text(
                            text = "餘額: $amount",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    1 -> Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable {
                                // 跳轉至圓餅圖頁面邏輯
                                navController.navigate("personal")
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        val income = viewModel.getUserIncome()
                        val expense = viewModel.getUserExpense()
                        val total = income + expense
                        val balance = income - expense
                        PieChart(income = income, expense = expense, balance = balance, total = total)
                    }
                }
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

            // 顯示時間最近的 4 個 Group，並利用 4 宮格顯示
            // 要包含群組照片加名稱
            // Do this

        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    // Create a mock NavController
    val navController = rememberNavController()
    // Create a mock or default MainViewModel
    val viewModel = MainViewModel() // You may need to provide required parameters or use a factory if necessary
    HomeScreen(navController = navController, onOpenDrawer = {}, viewModel = viewModel)
    // HomeScreen(navController = navController, onOpenDrawer = {})
}






