package com.example.billapp

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.billapp.group.GroupList
import com.example.billapp.models.Group
import com.example.billapp.models.User
import com.example.billapp.models.GroupTransaction
import com.example.billapp.personal.PersonalTransactionList
import com.example.billapp.viewModel.MainViewModel
import java.util.Calendar

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    onOpenDrawer: () -> Unit,
    viewModel: MainViewModel
) {
    val groups by viewModel.userGroups.collectAsState()
    // 获取最近两笔交易记录
    val transactions by viewModel.userTransactions.collectAsState()
    var filteredRecords by remember { mutableStateOf(transactions) }

    var selectedChart by remember { mutableStateOf("結餘") }
    val user by viewModel.user.collectAsState()
    fun filtered(){
        val filtered = transactions.filter {
            it.updatedAt != null

        }
            .sortedByDescending { it.updatedAt }
            .take(2)

        filteredRecords = filtered.filter { transaction ->
            when (selectedChart) {
                "支出" -> transaction.type == "支出"
                "收入" -> transaction.type == "收入"
                "結餘" -> true
                else -> true
            }
        }

    }

    LaunchedEffect(user) {
        user?.let {
            viewModel.loadUserTransactions()
            filtered()
        }
    }
    LaunchedEffect(transactions) {
        filtered()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "個人",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Left,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color(0xFFD9C2A7))
        ) {
            val userName = "getUserName()"
            val income = viewModel.getUserIncome()
            val expense = viewModel.getUserExpense()
            val total = income + expense
            val balance = income - expense

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(horizontal = 16.dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val pathLeft = Path().apply {
                        moveTo(0f, 0f)
                        lineTo(size.width * 0.6f, 0f)
                        lineTo(size.width * 0.4f, size.height)
                        lineTo(0f, size.height)
                        close()
                    }
                    val pathRight = Path().apply {
                        moveTo(size.width * 0.6f, 0f)
                        lineTo(size.width, 0f)
                        lineTo(size.width, size.height)
                        lineTo(size.width * 0.4f, size.height)
                        close()
                    }
                    drawPath(pathLeft, color = Color(0xFFD9C2A7))
                    drawPath(pathRight, color = Color(0xFFEAC75E))
                    drawLine(
                        color = Color.Gray,
                        start = Offset(size.width * 0.6f, 0f),
                        end = Offset(size.width * 0.4f, size.height),
                        strokeWidth = 4f
                    )
                }
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = userName,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 16.dp)
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f) // 確保 Box 是正方形
                            .padding(8.dp)
                    ) {
                        PieChart(
                            income = income,
                            expense = expense,
                            balance = balance,
                            total = total,
                            modifier = Modifier.size(60.dp) // 調整圓餅圖大小
                        )

                        // 顯示支出、收入和結餘
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(start = 4.dp, top = 4.dp) // 調整位置
                                .background(Color.White)
                                .border(1.dp, Color.Gray)
                                .padding(4.dp)
                        ) {
                            Text(
                                text = "支出: $expense",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(end = 4.dp, top = 4.dp) // 調整位置
                                .background(Color.White)
                                .border(1.dp, Color.Gray)
                                .padding(4.dp)
                        ) {
                            Text(
                                text = "收入: $income",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(start = 4.dp, bottom = 4.dp) // 調整位置
                                .background(Color.White)
                                .border(1.dp, Color.Gray)
                                .padding(4.dp)
                        ) {
                            Text(
                                text = "結餘: $balance",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }

        /*
        Spacer(modifier = Modifier.height(16.dp))

        Column(
                modifier = Modifier
                    .fillMaxWidth()

        ) {
            Text(
                text = "近期交易紀錄",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            PersonalTransactionList(
                transactions = filteredRecords,
                navController = navController,
                viewModel = viewModel
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "近期群組",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textAlign = TextAlign.Left,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        // 顯示時間最近的 4 個 Group，並利用 4 宮格顯示
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.height(320.dp)  // Adjust the height as needed
        ) {
            items(4) { index ->
                if (index < groups.size) {
                    GroupItem(group = groups[index], onItemClick = {
                        navController.navigate("groupDetail/${groups[index].id}")
                    })
                } else {
                    EmptyGroupSlot()
                }
            }
        }

 */
    }
}

// 只用於主頁
@Composable
fun GroupItem(group: Group, onItemClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable(onClick = onItemClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = group.image,
                contentDescription = "Group Image",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = group.name,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
@Composable
fun EmptyGroupSlot() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        // 空的內容，只顯示背景色
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

@Preview(showBackground = true)
@Composable
fun GroupItemPreview(){
    val group = Group(
        id = "1",
        name = "Group 1",
        image = "https://example.com/image1.jpg",
        createdBy = "User 1",
    )
    GroupItem(group = group, onItemClick = {})
}





