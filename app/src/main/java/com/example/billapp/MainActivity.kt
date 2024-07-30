// MainActivity.kt
package com.example.billapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import com.example.billapp.ui.theme.BillAppTheme
import com.example.myapplication.MainScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BillAppTheme {
                MainScreen()
            }
        }
    }
}