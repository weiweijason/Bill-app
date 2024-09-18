package com.example.billapp.activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.billapp.R
import com.example.billapp.firebase.FirestoreClass
import com.example.billapp.firebase.UserRepository
import com.example.billapp.models.User
import com.example.billapp.ui.theme.Brown2
import com.example.billapp.ui.theme.Brown3
import com.example.billapp.viewModel.SignUpUiState
import com.example.billapp.viewModel.SignUpViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    navController: NavController,
    viewModel: SignUpViewModel
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        // 背景圖片
        Image(
            painter = painterResource(id = R.drawable.ic_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp) // 頂層 padding
        ) {
            // Toolbar 標題
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.sign_up),
                        fontSize = 20.sp, // 設置標題字體大小
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF404040), // 主字體顏色
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color.Transparent)
            )

            Spacer(modifier = Modifier.height(32.dp)) // 間距

            // 說明文字
            Text(
                text = stringResource(id = R.string.sign_up_description_text),
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                textAlign = TextAlign.Center // 文字居中
            )

            // Card 內部內容
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp), // 外部 padding
                elevation = CardDefaults.cardElevation(8.dp), // 卡片陰影
                shape = RoundedCornerShape(16.dp), // 圓角
                colors = CardDefaults.cardColors(containerColor = Brown3) // 卡片背景色
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp) // 卡片內部 padding
                ) {
                    // 姓名輸入框
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text(stringResource(id = R.string.name)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        textStyle = TextStyle(fontSize = 16.sp)
                    )

                    // 郵箱輸入框
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text(stringResource(id = R.string.email)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        textStyle = TextStyle(fontSize = 16.sp)
                    )

                    // 密碼輸入框
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text(stringResource(id = R.string.password)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        textStyle = TextStyle(fontSize = 16.sp),
                        visualTransformation = PasswordVisualTransformation() // 密碼遮罩
                    )

                    // 註冊按鈕
                    Button(
                        onClick = {
                            if (validateForm(name, email, password)) {
                                viewModel.signUp(name, email, password)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Brown2) // 按鈕背景色
                    ) {
                        Text(
                            text = stringResource(id = R.string.sign_up),
                            color = Color.White,
                            fontSize = 18.sp
                        )
                    }
                }
            }

            // 處理不同的狀態
            when (val state = uiState) {
                is SignUpUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 16.dp)
                    )
                }
                is SignUpUiState.Success -> {
                    LaunchedEffect(state) {
                        navController.navigate("intro") {
                            popUpTo("signUp") { inclusive = true }
                        }
                    }
                }
                is SignUpUiState.Error -> {
                    Text(
                        text = state.message,
                        color = Color.Red,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
                else -> {}
            }
        }
    }
}

// 簡單驗證表單
private fun validateForm(name: String, email: String, password: String): Boolean {
    return name.isNotBlank() && email.isNotBlank() && password.isNotBlank()
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    val viewModel: SignUpViewModel = viewModel()
    SignUpScreen(navController = NavController(LocalContext.current), viewModel = viewModel)
}