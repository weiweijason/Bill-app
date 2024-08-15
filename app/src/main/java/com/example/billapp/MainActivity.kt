package com.example.billapp

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.billapp.ui.theme.BillAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 禁止螢幕旋轉
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        setContent {
            BillAppTheme {
                val navController = rememberNavController()
                // 設置群組名稱
                val groupName = "朋友聚會"

                // 傳遞參數給 GroupScreen
                NavigationComponent(navController = navController, groupName = groupName)
            }
        }
    }
}
