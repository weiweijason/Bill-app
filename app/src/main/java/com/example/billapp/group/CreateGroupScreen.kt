package com.example.billapp.group

import android.app.Application
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.billapp.viewModel.MainViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.billapp.R
import com.example.billapp.viewModel.AvatarViewModel
import com.example.billapp.viewModel.GroupCreationStatus
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGroup(
    viewModel: MainViewModel,
    avatarViewModel: AvatarViewModel,
    navController: NavController
) {
    var groupName by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var showBottomSheet by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val groupCreationStatus by viewModel.groupCreationStatus.collectAsState()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    Scaffold { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Create Group", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clickable { showBottomSheet = true }
                    .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = if (imageUri != null) {
                        rememberAsyncImagePainter(imageUri)
                    } else {
                        painterResource(id = R.drawable.ic_board_place_holder)
                    },
                    contentDescription = "Group Image",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = groupName,
                onValueChange = { groupName = it },
                label = { Text("Group Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.createGroup(groupName, imageUri, context)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Create Group")
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        showBottomSheet = false
                    }
                },
                sheetState = sheetState
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.6f)
                ) {
                    CreateGroupPresetAvatars(
                        onImageSelected = { uri ->
                            imageUri = uri
                            showBottomSheet = false
                        },
                        launcher = launcher
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                showBottomSheet = false
                            }
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("Cancel")
                    }
                }
            }
        }
    }

    LaunchedEffect(groupCreationStatus) {
        if (groupCreationStatus == GroupCreationStatus.SUCCESS) {
            navController.navigateUp()
            viewModel.resetGroupCreationStatus()
        }
    }
}

@Composable
fun CreateGroupPresetAvatars(
    onImageSelected: (Uri?) -> Unit,
    launcher: ManagedActivityResultLauncher<String, Uri?>
) {
    val presets = listOf(
        R.drawable.image_group_travel,
        R.drawable.image_group_house,
        R.drawable.image_group_dining,
        R.drawable.image_group_shopping
    )

    val context = LocalContext.current

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(8.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .padding(8.dp)
                    .clip(CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    .background(MaterialTheme.colorScheme.surface)
                    .clickable {
                        launcher.launch("image/*")
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.add_photo),
                    contentDescription = "Choose from gallery",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(40.dp)
                )
            }
        }
        items(presets) { preset ->
            Image(
                painter = painterResource(id = preset),
                contentDescription = "Preset Group Avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .padding(8.dp)
                    .clip(CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    .clickable {
                        val uri = Uri.parse("android.resource://${context.packageName}/$preset")
                        onImageSelected(uri)
                    }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateGroupPreview() {
    // Create a mock NavController
    val navController = rememberNavController()
    // Create a mock or default MainViewModel
    val viewModel = MainViewModel() // You may need to provide required parameters or use a factory if necessary
    val avatarViewModel = AvatarViewModel(LocalContext.current.applicationContext as Application)
    CreateGroup(navController = navController, viewModel = viewModel, avatarViewModel = avatarViewModel)
}