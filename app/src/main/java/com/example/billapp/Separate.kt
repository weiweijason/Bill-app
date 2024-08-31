package com.example.billapp


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.billapp.R
import com.example.billapp.viewModel.MainViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.mutableStateListOf




@Composable
fun SeparateScreen(
    navController: NavController,
    viewModel: MainViewModel,
    groupId: String,
    amount: Float
) {
    LaunchedEffect(groupId) {
        viewModel.getGroupMembers(groupId)
    }

    val groupMembers by viewModel.groupMembers.collectAsState()

    // Ensure that checkedStates has the same size as groupMembers
    val checkedStates = remember(groupMembers) {
        mutableStateListOf<Boolean>().apply {
            addAll(List(groupMembers.size) { false })
        }
    }

    var selectedTab by remember { mutableStateOf("均分") }
    var isEvenSplitSelected by remember { mutableStateOf(false) } // 設定 isEvenSplitSelected 狀態

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFD9C9BA)) // 设置页面的背景颜色
            .padding(16.dp)
    ) {
        // 顶部带返回按钮的标题
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 返回按钮
            IconButton(onClick = {
                navController.popBackStack() // This will navigate back to the previous screen in the back stack
            }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.Black
                )
            }

            Spacer(modifier = Modifier.weight(1f)) // 占用剩余空间以居中标题

            // 居中的标题
            Text(
                text = "分帳選項",
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(2f)
            )

            Spacer(modifier = Modifier.weight(1f)) // 左右对称的间隔
        }

        // 选项卡 (按比例/按調整/按金額/按份數)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { selectedTab = "均分" },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedTab == "均分") colorResource(id = R.color.colorAccent) else colorResource(id = R.color.primary_text_color)
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text("均分")
            }
            Button(
                onClick = { selectedTab = "比例" },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedTab == "比例") colorResource(id = R.color.colorAccent) else colorResource(id = R.color.primary_text_color)
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text("比例")
            }
            Button(
                onClick = { selectedTab = "調整" },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedTab == "調整") colorResource(id = R.color.colorAccent) else colorResource(id = R.color.primary_text_color)
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text("調整")
            }
            Button(
                onClick = { selectedTab = "金額" },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedTab == "金額") colorResource(id = R.color.colorAccent) else colorResource(id = R.color.primary_text_color)
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text("金額")
            }
            Button(
                onClick = { selectedTab = "份數" },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedTab == "份數") colorResource(id = R.color.colorAccent) else colorResource(id = R.color.primary_text_color)
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text("份數")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 根据选中的选项卡显示相应的内容
        when (selectedTab) {
            "均分" -> {
                Column {
                    Text(
                        text = "均分",
                        fontSize = 24.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "依照人數均分。",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White) // 白色背景
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "金額: ${"%.2f".format(amount)}",
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                    }

                    // LazyColumn to list group members with Checkboxes
                    LazyColumn {
                        itemsIndexed(groupMembers) { index, member ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = checkedStates[index],
                                    onCheckedChange = { isChecked ->
                                        checkedStates[index] = isChecked
                                    }
                                )
                                Text(
                                    text = member.name,
                                    fontSize = 18.sp,
                                    modifier = Modifier
                                        .weight(1f)
                                        .background(Color.White)
                                        .padding(8.dp)
                                )
                                if (checkedStates[index]) {
                                    val splitAmount = (amount / checkedStates.count { it }).let {
                                        "%.2f".format(it)
                                    }
                                    Text(
                                        text = "$$splitAmount",
                                        fontSize = 18.sp,
                                        modifier = Modifier
                                            .padding(end = 8.dp)
                                            .align(Alignment.CenterVertically)
                                    )
                                }
                            }
                        }
                    }

                    // Submit Button
                    Button(
                        onClick = {
                            val selectedUsers = groupMembers.filterIndexed { index, _ ->
                                checkedStates[index]
                            }
                            // Call your viewModel function here
                            viewModel.addGroupTransaction(groupId)
                            navController.popBackStack()
                        },
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(16.dp)
                    ) {
                        Text("完成")
                    }
                }
            }
            "比例" -> {
                Column {
                    Text(
                        text = "按比例",
                        fontSize = 24.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "依照每人比例分配",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White) // 白色背景
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "金額: ${"%.2f".format(amount)}",
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                    }
                    // LazyColumn to list group members with Percentage Input
                    LazyColumn {
                        itemsIndexed(groupMembers) { index, member ->
                            var percentage by remember { mutableStateOf("") }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = checkedStates[index],
                                    onCheckedChange = { isChecked ->
                                        checkedStates[index] = isChecked
                                    }
                                )
                                Text(
                                    text = member.name,
                                    fontSize = 18.sp,
                                    modifier = Modifier
                                        .weight(1f)
                                        .background(Color.White)
                                        .padding(8.dp)
                                )

                                if (checkedStates[index]) {
                                    // Percentage input with TextField
                                    OutlinedTextField(
                                        value = percentage,
                                        onValueChange = { newPercentage ->
                                            if (newPercentage.isEmpty() || newPercentage.toFloatOrNull() != null) {
                                                percentage = newPercentage
                                            }
                                        },
                                        label = { Text("%") },
                                        modifier = Modifier
                                            .width(80.dp)
                                            .padding(horizontal = 8.dp),
                                        singleLine = true
                                    )

                                    // Display calculated amount
                                    val calculatedAmount = (percentage.toFloatOrNull() ?: 0f) / 100 * amount
                                    Text(
                                        text = "$${"%.2f".format(calculatedAmount)}",
                                        fontSize = 18.sp,
                                        modifier = Modifier
                                            .padding(end = 8.dp)
                                            .align(Alignment.CenterVertically)
                                    )
                                }
                            }
                        }
                    }

                    // Submit Button
                    Button(
                        onClick = {
                            val selectedUsers = groupMembers.filterIndexed { index, _ ->
                                checkedStates[index]
                            }
                            // Call your viewModel function here
                            viewModel.addGroupTransaction(groupId)
                            navController.popBackStack()
                        },
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(16.dp)
                    ) {
                        Text("完成")
                    }

                }
            }
            "調整" -> {
                Column {
                    Text(
                        text = "按調整",
                        fontSize = 24.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "扣除每人額外的花費後，剩下部分所有人均分。",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White) // 白色背景
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "金額: ${"%.2f".format(amount)}",
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                    }

                    // Store user-entered amounts in a list
                    val userEnteredAmounts = remember { mutableStateListOf<Float?>() }
                    groupMembers.forEach { _ -> userEnteredAmounts.add(null) }

                    // LazyColumn to list group members with amount input
                    LazyColumn {
                        itemsIndexed(groupMembers) { index, member ->
                            var enteredAmount by remember { mutableStateOf("") }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = checkedStates[index],
                                    onCheckedChange = { isChecked ->
                                        checkedStates[index] = isChecked
                                    }
                                )
                                Text(
                                    text = member.name,
                                    fontSize = 18.sp,
                                    modifier = Modifier
                                        .weight(1f)
                                        .background(Color.White)
                                        .padding(8.dp)
                                )

                                if (checkedStates[index]) {
                                    // Amount input with TextField
                                    OutlinedTextField(
                                        value = enteredAmount,
                                        onValueChange = { newAmount ->
                                            if (newAmount.isEmpty() || newAmount.toFloatOrNull() != null) {
                                                enteredAmount = newAmount
                                                userEnteredAmounts[index] = enteredAmount.toFloatOrNull()
                                            }
                                        },
                                        label = { Text("金額") },
                                        modifier = Modifier
                                            .width(100.dp)
                                            .padding(horizontal = 8.dp),
                                        singleLine = true
                                    )

                                    // Calculate final amount
                                    val totalEnteredAmount = userEnteredAmounts.filterNotNull().sum()
                                    val remainingAmount = amount - totalEnteredAmount
                                    val evenlySplitAmount = if (checkedStates.count { it } > 0) remainingAmount / checkedStates.count { it } else 0f
                                    val finalAmount = (userEnteredAmounts[index] ?: 0f) + evenlySplitAmount

                                    // Display calculated amount aligned to the right
                                    Text(
                                        text = "$${"%.2f".format(finalAmount)}",
                                        fontSize = 18.sp,
                                        modifier = Modifier
                                            .padding(end = 8.dp)
                                            .weight(1f)
                                            .align(Alignment.CenterVertically),
                                        textAlign = TextAlign.End
                                    )
                                }
                            }
                        }
                    }

                    // Submit Button
                    Button(
                        onClick = {
                            val selectedUsers = groupMembers.filterIndexed { index, _ ->
                                checkedStates[index]
                            }
                            // Call your viewModel function here
                            viewModel.addGroupTransaction(groupId)
                            navController.popBackStack()
                        },
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(16.dp)
                    ) {
                        Text("完成")
                    }
                }
            }
            "金額" -> {
                Column {
                    Text(
                        text = "按確切金額",
                        fontSize = 24.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "按照每人實際花費分配",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White) // 白色背景
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "金額: ${"%.2f".format(amount)}",
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                    }

                    // Store user-entered amounts in a list
                    val userEnteredAmounts = remember { mutableStateListOf<Float?>() }
                    groupMembers.forEach { _ -> userEnteredAmounts.add(null) }

                    // LazyColumn to list group members with Amount Input
                    LazyColumn {
                        itemsIndexed(groupMembers) { index, member ->
                            var enteredAmount by remember { mutableStateOf("") }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = checkedStates[index],
                                    onCheckedChange = { isChecked ->
                                        checkedStates[index] = isChecked
                                    }
                                )
                                Text(
                                    text = member.name,
                                    fontSize = 18.sp,
                                    modifier = Modifier
                                        .weight(1f)
                                        .background(Color.White)
                                        .padding(8.dp)
                                )

                                if (checkedStates[index]) {
                                    // Amount input with TextField
                                    OutlinedTextField(
                                        value = enteredAmount,
                                        onValueChange = { newAmount ->
                                            if (newAmount.isEmpty() || newAmount.toFloatOrNull() != null) {
                                                enteredAmount = newAmount
                                                userEnteredAmounts[index] = enteredAmount.toFloatOrNull()
                                            }
                                        },
                                        label = { Text("金額") },
                                        modifier = Modifier
                                            .width(100.dp)
                                            .padding(horizontal = 8.dp),
                                        singleLine = true
                                    )
                                }

                                // Display the entered amount in the same row aligned to the left
                                Text(
                                    text = "$${"%.2f".format(userEnteredAmounts[index] ?: 0f)}",
                                    fontSize = 18.sp,
                                    modifier = Modifier
                                        .padding(start = 8.dp)
                                        .align(Alignment.CenterVertically)
                                )
                            }
                        }
                    }

                    // Submit Button
                    Button(
                        onClick = {
                            val selectedUsers = groupMembers.filterIndexed { index, _ ->
                                checkedStates[index]
                            }
                            // Call your viewModel function here
                            viewModel.addGroupTransaction(groupId)
                            navController.popBackStack()
                        },
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(16.dp)
                    ) {
                        Text("完成")
                    }
                }
            }
            "份數" -> {
                Column {
                    Text(
                        text = "按份數",
                        fontSize = 24.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "按照每人持有的份數分配",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White) // 白色背景
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "金額: ${"%.2f".format(amount)}",
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                    }
                    // Store user-entered shares in a list
                    val userEnteredShares = remember { mutableStateListOf<Int?>() }
                    groupMembers.forEach { _ -> userEnteredShares.add(null) }

                    // LazyColumn to list group members with Shares Input
                    LazyColumn {
                        itemsIndexed(groupMembers) { index, member ->
                            var enteredShares by remember { mutableStateOf("") }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = checkedStates[index],
                                    onCheckedChange = { isChecked ->
                                        checkedStates[index] = isChecked
                                    }
                                )
                                Text(
                                    text = member.name,
                                    fontSize = 18.sp,
                                    modifier = Modifier
                                        .weight(1f)
                                        .background(Color.White)
                                        .padding(8.dp)
                                )

                                if (checkedStates[index]) {
                                    // Shares input with TextField
                                    OutlinedTextField(
                                        value = enteredShares,
                                        onValueChange = { newShares ->
                                            if (newShares.isEmpty() || newShares.toIntOrNull() != null) {
                                                enteredShares = newShares
                                                userEnteredShares[index] = enteredShares.toIntOrNull()
                                            }
                                        },
                                        label = { Text("份數") },
                                        modifier = Modifier
                                            .width(80.dp)
                                            .padding(horizontal = 8.dp),
                                        singleLine = true
                                    )
                                }

                                // Display the calculated amount in the same row aligned to the left
                                val totalShares = userEnteredShares.filterNotNull().sum()
                                val calculatedAmount = if (totalShares > 0 && checkedStates[index]) {
                                    (userEnteredShares[index] ?: 0) / totalShares.toFloat() * amount
                                } else {
                                    0f
                                }
                                Text(
                                    text = "$${"%.2f".format(calculatedAmount)}",
                                    fontSize = 18.sp,
                                    modifier = Modifier
                                        .padding(start = 8.dp)
                                        .align(Alignment.CenterVertically)
                                )
                            }
                        }
                    }

                    // Submit Button
                    Button(
                        onClick = {
                            val selectedUsers = groupMembers.filterIndexed { index, _ ->
                                checkedStates[index]
                            }
                            // Call your viewModel function here
                            viewModel.addGroupTransaction(groupId)
                            navController.popBackStack()
                        },
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(16.dp)
                    ) {
                        Text("完成")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSeparateScreen() {
    val navController = rememberNavController()
    val viewModel = MainViewModel() // Assuming a default constructor is available
    //val groupId = "test" // Use a sample groupId for preview purposes
    val amount = 100.0f
    SeparateScreen(navController, viewModel, "test", amount)
}

