package com.example.billapp

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.billapp.ui.theme.BillAppTheme

@Composable
fun NavigationComponent(navController: NavHostController, groupName: String) {
    NavHost(navController = navController, startDestination = "groupScreen") {
        composable("groupScreen") {
            GroupScreen(groupName = groupName, navController = navController)
        }
        composable("memberListScreen") {
            MemberListScreen(navController = navController)
        }
        composable("groupInviteLinkScreen") {
            GroupInviteLinkScreen()
        }
    }
}
