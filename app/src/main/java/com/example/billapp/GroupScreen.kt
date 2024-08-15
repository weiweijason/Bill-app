package com.example.billapp

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.billapp.ui.theme.BillAppTheme

@Composable
fun GroupScreen(groupName: String, navController: NavController) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Text(
                text = groupName,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                fontSize = 24.sp
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
                    .padding(horizontal = 16.dp, vertical = 16.dp) // Increased padding
                    .background(Color.White)
                    .border(2.dp, Color.Gray)
                    .padding(24.dp) // Increased internal padding
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
                            // Navigate to 成員 screen
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
                            // Navigate to 群組邀請連結 screen
                            navController.navigate("groupInviteLinkScreen")
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "群組邀請連結")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GroupScreenPreview() {
    BillAppTheme {
        val navController = rememberNavController()
        GroupScreen(
            groupName = "範例群組",
            navController = navController
        )
    }
}
