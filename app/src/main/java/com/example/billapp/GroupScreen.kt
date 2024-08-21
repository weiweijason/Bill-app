package com.example.billapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.billapp.viewModel.MainViewModel
import com.example.billapp.R
import com.example.billapp.models.Group

@Composable
fun GroupSettingScreen(
    groupId: String,
    viewModel: MainViewModel,
    navController: NavController
) {
    val group by viewModel.getGroup(groupId).collectAsState(initial = null)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            androidx.compose.material.TopAppBar(
                title = { androidx.compose.material.Text(group?.name ?: "Board Detail") },
                navigationIcon = {
                    androidx.compose.material.IconButton(onClick = { navController.navigateUp() }) {
                        androidx.compose.material.Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Back"
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
        ) {
            // 債務關係區塊
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .background(Color.White)
                    .border(2.dp, Color.Gray)
                    .padding(24.dp)
            ) {
                Text(
                    text = "債務關係",
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 20.sp
                )
            }

            // 管理員區塊
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .background(Color.LightGray)
                    .border(2.dp, Color.Gray)
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "管理員",
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // 成員按鈕
                    Button(
                        onClick = {
                            navController.navigate("memberListScreen")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    ) {
                        Text(text = "成員")
                    }

                    // 群組邀請連結按鈕
                    Button(
                        onClick = {
                            navController.navigate("groupInviteLinkScreen")
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "群組邀請連結")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Delete Group Button
                    Button(
                        onClick = {
                            group?.let {
                                viewModel.deleteGroup(it.id)
                                navController.navigateUp() // Navigate back after deletion
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red, // Background color
                            contentColor = Color.White  // Text and icon color
                        )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_delete), // Your delete icon resource
                            contentDescription = "Delete Group"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "刪除群組")
                    }
                }
            }
        }
    }
}



// 下方群組圖示點擊後會導到 GroupScreen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupScreen(
    navController: NavController,
    viewModel: MainViewModel,
) {
    val groups = remember { mutableStateListOf<String>() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "群組",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        color = Color.Black
                    ) },
                actions = {
                    IconButton(onClick = { navController.navigate("Join_Group") }) {
                        Icon(Icons.Default.Add, contentDescription = "加入群組")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { navController.navigate("addItemScreen") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(text = "新增群組", fontSize = 18.sp)
            }

            Button(onClick = { navController.navigate("Group_Invite") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(text = "test", fontSize = 18.sp)
            }

            LazyColumn {
                items(groups) { group ->
                    Text(
                        text = group,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )
                }
            }
        }
    }
}


// 顯示在主頁的 Group，會放在底下 GroupList 中
@Composable
fun GroupItem(groupName: String, createdBy: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp) // External padding
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp), // Rounded corners
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), // Internal padding
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Circular ImageView
            Image(
                painter = painterResource(id = R.drawable.ic_board_place_holder),
                contentDescription = stringResource(id = R.string.image_contentDescription),
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Group name and created by text
            Column {
                BasicText(
                    text = groupName,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = MaterialTheme.typography.headlineSmall.fontSize // Larger font size
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                BasicText(
                    text = "created by : $createdBy",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun GroupList(
    boardItems: List<Group>,
    onGroupClick: (String) -> Unit,
    navController: NavController
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(boardItems) { groupItem ->
            GroupItem(
                groupName = groupItem.name,
                createdBy = groupItem.createdBy,
                onClick = { onGroupClick(groupItem.id) }
            )
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    navController.navigate("CreateGroupScreen")
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

@Preview
@Composable
fun GroupItemPreview()
{
    GroupItem("Travel","Jason",{})
}

