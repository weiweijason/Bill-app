package com.example.billapp

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.billapp.models.Group
import com.example.billapp.models.GroupTransaction
import com.example.billapp.models.User

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen() {
    var showAddItemScreen by remember { mutableStateOf(false) }
    var groups by remember { mutableStateOf(listOf<Group>()) }

    if (showAddItemScreen) {
        AddItemScreen(
            onAddItem = { item ->
                val defaultUserA = User(id = "1", name = "A", image = "")
                val defaultUserB = User(id = "2", name = "B", image = "")
                val defaultTransaction = GroupTransaction(from = defaultUserA, to = defaultUserB, amount = 100.0)
                groups = groups + Group(name = item, transactions = listOf(defaultTransaction)) // 新增群組
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

                val pagerState = rememberPagerState(pageCount = {2})
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
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "餘額: $0",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        1 -> Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable {
                                    // 跳轉至圓餅圖頁面邏輯
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            // 空圓圈
                            Canvas(modifier = Modifier.size(100.dp)) {
                                drawCircle(
                                    color = Color.Gray,
                                    radius = size.minDimension / 2,
                                    style = Stroke(width = 4.dp.toPx())
                                )
                            }
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
                        GroupItem(group = group)
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

@Composable
fun GroupItem(group: Group) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {  }
    ) {
        Text(
            text = group.name,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        group.transactions.forEach { transaction ->
            TransactionItem(transaction = transaction)
        }
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
            UserAvatar(user = transaction.from)
            Text(
                text = transaction.from.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
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
            UserAvatar(user = transaction.to)
            Text(
                text = transaction.to.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
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