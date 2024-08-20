package com.example.billapp

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.billapp.models.Group
import com.example.billapp.models.User
import com.example.billapp.models.GroupTransaction
import com.example.billapp.viewModel.MainViewModel
import kotlinx.coroutines.launch
import kotlin.math.exp

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
    val groups = listOf(
        Group("Group 1", "", "", listOf(), listOf(), listOf()),
        Group("Group 2", "", "", listOf(), listOf(), listOf())
    )
    var showAddItemScreen by remember { mutableStateOf(false) }

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
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                items(groups) { group ->
                    GroupItem(group = group, viewModel = viewModel)
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            navController.navigate("addItemScreen")
                        },
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

@Composable
fun PieChart(income: Float, expense: Float, balance: Float, total: Float) {
    val incomeAngle = (income / total) * 360f
    val expenseAngle = (expense / total) * 360f

    Box(contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(200.dp)) {
            withTransform({
                rotate(270f)
            }) {
                drawArc(
                    color = Color.Green,
                    startAngle = 0f,
                    sweepAngle = incomeAngle,
                    useCenter = false,
                    style = Stroke(width = 20.dp.toPx())
                )
                drawArc(
                    color = Color.Red,
                    startAngle = incomeAngle,
                    sweepAngle = expenseAngle,
                    useCenter = false,
                    style = Stroke(width = 20.dp.toPx())
                )
            }
        }
        Text(
            text = "結餘: $balance",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun GroupItem(group: Group, viewModel: MainViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { }
    ) {
        Text(
            text = group.name,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        // group.transactions?.forEach { transaction ->
        //     TransactionItem(transaction = transaction)
        // }
    }
}

@Composable
fun TransactionItem(transaction: GroupTransaction) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {

        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "${transaction.amount} 元",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "----->",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {

        }
    }
}

@Composable
fun UserAvatar(user: User) {
    val avatarResource = if (user.id == "2") {
        R.drawable.avatar_placeholder_2
    } else {
        R.drawable.avatar_placeholder
    }

    Image(
        painter = painterResource(id = avatarResource),
        contentDescription = user.name,
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape),
        contentScale = ContentScale.Crop
    )
}