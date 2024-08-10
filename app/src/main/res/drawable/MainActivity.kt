package com.example.ProjectManager

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ProjectManager.activity.IntroActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainScreen(onLogOut = { logOut() })
        }
    }

    private fun logOut() {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, IntroActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(onLogOut: () -> Unit) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                val onCloseDrawer: () -> Unit = {
                    scope.launch {
                        drawerState.close()
                    }
                }
                DrawerContent(navController, onCloseDrawer, onLogOut)
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("My App") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menu")
                        }
                    }
                )
            }
        ) { innerPadding ->
            // Main content
            NavHost(
                navController = navController,
                startDestination = "main",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("main") { MainContent() }
                composable("profile") { ProfileScreen() }
                // 添加其他需要的路由
            }
        }
    }
}

@Composable
fun DrawerContent(navController: NavController, onCloseDrawer: () -> Unit, onLogOut: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = R.drawable.ic_user_place_holder),
                    contentDescription = "User Image",
                    modifier = Modifier.size(100.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text("Username")
            }
        }
        Divider()

        // Menu items
        Spacer(modifier = Modifier.height(16.dp))
        NavigationDrawerItem(
            icon = { Icon(painter = painterResource(id = R.drawable.ic_nav_user), contentDescription = null) },
            label = { Text("My Profile") },
            selected = false,
            onClick = {
                navController.navigate("profile")
                onCloseDrawer()
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        NavigationDrawerItem(
            icon = { Icon(painter = painterResource(id = R.drawable.ic_nav_sign_out), contentDescription = null) },
            label = { Text("Sign Out") },
            selected = false,
            onClick = {
                onLogOut()
            }
        )
    }
}

@Composable
fun ProfileScreen() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("ProfileScreen")
        }
    }
}

@Composable
fun MainContent(){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("MainContent")
        }
    }
}

@Preview
@Composable
fun MainScreenPreview()
{
    MainScreen {

    }
}