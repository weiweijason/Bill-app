package com.example.billapp

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.billapp.ui.theme.BillAppTheme

@Composable
fun GroupInviteLinkScreen() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Text(
                text = "群組邀請連結",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                fontSize = 24.sp
            )
        }
    ) { innerPadding ->
        // 這裡會顯示邀請連結
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Add invite link UI here
            Text(text = "邀請連結： https://example.com/invite")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GroupInviteLinkScreenPreview() {
    BillAppTheme {
        GroupInviteLinkScreen()
    }
}
