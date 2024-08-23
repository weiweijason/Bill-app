package com.example.billapp.group

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.billapp.dept_relation.DeptRelationsScreen
import com.example.billapp.R
import com.example.billapp.viewModel.MainViewModel

@Composable
fun GroupSettingScreen(
    groupId: String,
    viewModel: MainViewModel,
    navController: NavController
) {
    val group by viewModel.getGroup(groupId).collectAsState(initial = null)
    val deptRelations by viewModel.getDeptRelations(groupId).collectAsState(initial = emptyList())

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            androidx.compose.material.TopAppBar(
                title = { androidx.compose.material.Text(group?.name ?: "Group Detail") },
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
            // 債務關係區塊 (Debt Relationship Section)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                elevation = 4.dp,
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(2.dp, Color.Gray),
                backgroundColor = Color.White
            ) {
                Column(modifier = Modifier.padding(16.dp))
                {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp), // Adds padding inside the Card
                        verticalAlignment = Alignment.CenterVertically, // Centers content vertically
                        horizontalArrangement = Arrangement.SpaceBetween // Ensures space between items
                    ) {
                        Text(
                            text = "債務關係",
                            style = MaterialTheme.typography.bodyLarge,
                            fontSize = 20.sp,
                            modifier = Modifier
                                .weight(1f) // Makes the Text take available space
                                .padding(end = 8.dp) // Adds space between Text and Button
                        )

                        Button(
                            onClick = {
                                navController.navigate("groupTest/$groupId")
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary // Use theme color for consistency
                            ),
                            shape = RoundedCornerShape(8.dp), // Rounded corners for the button
                            modifier = Modifier
                                .weight(1f) // Makes the Button take available space
                                .padding(start = 16.dp) // Adds space between Button and Text
                                .fillMaxWidth() // Ensures the button is as wide as the available space
                        ) {
                            Text(
                                text = "新增交易",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                    DeptRelationsScreen(viewModel = viewModel, groupId = groupId)
                }
            }

            // 管理員區塊 (Administrator Section)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                elevation = 4.dp,
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(2.dp, Color.Gray),
                backgroundColor = Color.LightGray
            ) {
                Column(
                    modifier = Modifier.padding(16.dp) // Padding inside the Card
                ) {
                    Text(
                        text = "管理員",
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // 成員按鈕 (Member Button)
                    Button(
                        onClick = {
                            // Navigate to 成員 screen
                            navController.navigate("memberListScreen/$groupId")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    ) {
                        Text(text = "成員")
                    }

                    // 群組邀請連結按鈕 (Group Invite Link Button)
                    Button(
                        onClick = {
                            // Navigate to 群組邀請連結 screen
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
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red, // Background color
                            contentColor = Color.White // Text and icon color
                        ),
                        shape = RoundedCornerShape(8.dp) // Rounded corners for the button
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

@Preview(showBackground = true)
@Composable
fun GroupSettingScreenPreview() {
    // Create a mock NavController
    val navController = rememberNavController()

    // Create a mock or default MainViewModel
    val viewModel = MainViewModel() // You may need to provide required parameters or use a factory if necessary

    // Call the composable you want to preview
    GroupSettingScreen(
        groupId = "mockGroupId",
        viewModel = viewModel,
        navController = navController
    )
}