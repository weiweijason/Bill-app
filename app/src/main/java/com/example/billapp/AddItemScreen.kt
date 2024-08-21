@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.billapp

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.billapp.viewModel.MainViewModel

@Composable
fun AddItemScreen(
    navController: NavController,
    onAddItem: (String, Int?) -> Unit,
    imageViewModel: ImageViewModel = viewModel()
) {
    var text by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    var selectedImage by remember { mutableStateOf(R.drawable.image1) } // 預設圖片

    // 獲取 LocalContext
    val context = LocalContext.current

    // 監聽返回的圖片資源 ID 或 URI
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val savedStateHandle = navBackStackEntry?.savedStateHandle

    navBackStackEntry?.let {
        val savedStateHandle = it.savedStateHandle
        savedStateHandle.getLiveData<Int>("selectedImage")?.observe(it) { imageResId ->
            selectedImage = imageResId
            imageViewModel.selectedImageBitmap.value = null
            imageViewModel.selectedImageUri.value = null
        }
        savedStateHandle.getLiveData<Uri>("selectedImageUri")?.observe(it) { uri ->
            imageViewModel.setImageUri(uri, context)
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Group") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (imageViewModel.selectedImageBitmap.value != null) {
                Image(
                    bitmap = imageViewModel.selectedImageBitmap.value!!,
                    contentDescription = "Selected Image",
                    modifier = Modifier.size(100.dp)
                )
            } else {
                Image(
                    painter = painterResource(id = selectedImage),
                    contentDescription = "Selected Image",
                    modifier = Modifier.size(100.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navController.navigate("selectImageScreen") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Select Image")
            }
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = text,
                onValueChange = {
                    text = it
                    isError = text.isBlank()
                },
                label = { Text("Group Name") },
                modifier = Modifier.fillMaxWidth(),
                isError = isError,
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
            )
            if (isError) {
                Text(
                    text = "Group name cannot be empty",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (text.isNotBlank()) {
                        onAddItem(text, selectedImage)
                        navController.navigateUp() // Navigate back after adding item
                    } else {
                        isError = true
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = text.isNotBlank()
            ) {
                Text("Add")
            }
        }
    }
}


@Preview
@Composable
fun AddItemScreenPreview(){
    // Create a mock NavController
    val navController = rememberNavController()
    // Create a mock or default MainViewModel
    val viewModel = MainViewModel() // You may need to provide required parameters or use a factory if necessary

    AddItemScreen(navController = navController, onAddItem = { _, _ -> })
}