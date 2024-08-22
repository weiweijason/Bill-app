package com.example.billapp

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.billapp.models.TransactionCategory
import com.example.billapp.viewModel.MainViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StylishTextField2(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    val capybaraBrown = colorResource(id = R.color.colorAccent)

    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        textStyle = TextStyle(
            fontSize = 18.sp,
            fontFamily = FontFamily.Cursive,
            color = capybaraBrown
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(2.dp, capybaraBrown, RoundedCornerShape(8.dp)),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = colorResource(id = R.color.colorLight),
            focusedIndicatorColor = capybaraBrown,
            unfocusedIndicatorColor = Color.Transparent
        ),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        keyboardActions = KeyboardActions(onAny = { /* 禁止默认键盘动作 */ })
    )
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownMenuField2(
    label: String,
    selectedItem: String,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val items = listOf("選項 1", "選項 2", "選項 3")

    val capybaraBrown = colorResource(id = R.color.colorAccent)

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = selectedItem,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = colorResource(id = R.color.colorLight),
                unfocusedContainerColor = colorResource(id = R.color.colorLight),
                focusedIndicatorColor = capybaraBrown,
                unfocusedIndicatorColor = Color.Transparent
            ),
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp)
                .border(2.dp, capybaraBrown, RoundedCornerShape(8.dp))
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption) },
                    onClick = {
                        onItemSelected(selectionOption)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun CustomKeyboard2(
    onKeyClick: (String) -> Unit,
    onDeleteClick: () -> Unit,
    onClearClick: () -> Unit,
    onOkClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val capybaraBrown = colorResource(id = R.color.colorAccent)
    val capybaraLight = colorResource(id = R.color.colorLight)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        val buttonModifier = Modifier
            .weight(1f)
            .padding(4.dp)

        val buttonColors = ButtonDefaults.buttonColors(
            containerColor = capybaraLight
        )

        // 鍵盤按鈕
        Row(modifier = Modifier.fillMaxWidth()) {
            Button(modifier = buttonModifier, onClick = { onKeyClick("7") }, colors = buttonColors) {
                Text("7", color = capybaraBrown)
            }
            Button(modifier = buttonModifier, onClick = { onKeyClick("8") }, colors = buttonColors) {
                Text("8", color = capybaraBrown)
            }
            Button(modifier = buttonModifier, onClick = { onKeyClick("9") }, colors = buttonColors) {
                Text("9", color = capybaraBrown)
            }
            Button(modifier = buttonModifier, onClick = { onKeyClick("÷") }, colors = buttonColors) {
                Text("÷", color = capybaraBrown)
            }
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            Button(modifier = buttonModifier, onClick = { onKeyClick("4") }, colors = buttonColors) {
                Text("4", color = capybaraBrown)
            }
            Button(modifier = buttonModifier, onClick = { onKeyClick("5") }, colors = buttonColors) {
                Text("5", color = capybaraBrown)
            }
            Button(modifier = buttonModifier, onClick = { onKeyClick("6") }, colors = buttonColors) {
                Text("6", color = capybaraBrown)
            }
            Button(modifier = buttonModifier, onClick = { onKeyClick("×") }, colors = buttonColors) {
                Text("×", color = capybaraBrown)
            }
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            Button(modifier = buttonModifier, onClick = { onKeyClick("1") }, colors = buttonColors) {
                Text("1", color = capybaraBrown)
            }
            Button(modifier = buttonModifier, onClick = { onKeyClick("2") }, colors = buttonColors) {
                Text("2", color = capybaraBrown)
            }
            Button(modifier = buttonModifier, onClick = { onKeyClick("3") }, colors = buttonColors) {
                Text("3", color = capybaraBrown)
            }
            Button(modifier = buttonModifier, onClick = { onKeyClick("-") }, colors = buttonColors) {
                Text("-", color = capybaraBrown)
            }
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            Button(modifier = buttonModifier, onClick = { onKeyClick(".") }, colors = buttonColors) {
                Text(".", color = capybaraBrown)
            }
            Button(modifier = buttonModifier, onClick = { onKeyClick("0") }, colors = buttonColors) {
                Text("0", color = capybaraBrown)
            }
            Button(modifier = buttonModifier, onClick = onDeleteClick, colors = buttonColors) {
                Text("⌫", color = capybaraBrown)
            }
            Button(modifier = buttonModifier, onClick = { onKeyClick("+") }, colors = buttonColors) {
                Text("+", color = capybaraBrown)
            }
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            Button(modifier = buttonModifier, onClick = onClearClick, colors = buttonColors) {
                Text("AC", color = capybaraBrown)
            }
            Button(modifier = buttonModifier, onClick = onOkClick, colors = buttonColors) {
                Text("OK", color = capybaraBrown)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemAdd2(
    navController: NavController,
    viewModel: MainViewModel
) {
    val transactionType by viewModel.transactionType.collectAsState()
    val amount by viewModel.amount.collectAsState()
    val category by viewModel.category.collectAsState()
    val name by viewModel.name.collectAsState()
    val note by viewModel.note.collectAsState()
    var selectedCategory by remember { mutableStateOf(TransactionCategory.FOOD) }

    var selectedTab by remember { mutableStateOf("個人") }
    var groupName by remember { mutableStateOf("") }
    var payer by remember { mutableStateOf("") }
    var splitMethod by remember { mutableStateOf("平均分攤") }

    val coroutineScope = rememberCoroutineScope()
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()
    val keyboardController = LocalSoftwareKeyboardController.current

    var isBottomSheetVisible by remember { mutableStateOf(false) }
    // Dropdown menu expanded state
    var expanded by remember { mutableStateOf(false) }

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            if (isBottomSheetVisible) {
                CustomKeyboard2(
                    onKeyClick = { key ->
                        val currentAmount = amount.toString()
                        val newAmount = when (key) {
                            "÷", "×", "+", "-" -> {
                                if (currentAmount.isEmpty()) key else "$currentAmount $key "
                            }
                            "." -> {
                                if (currentAmount.contains(".")) currentAmount else "$currentAmount$key"
                            }
                            else -> "$currentAmount$key"
                        }
                        val newAmountValue = newAmount.toDoubleOrNull() ?: 0.0
                        viewModel.setAmount(newAmountValue)
                    },
                    onDeleteClick = {
                        val currentAmount = amount.toString()
                        val newAmount = currentAmount.dropLast(1)
                        val newAmountValue = newAmount.toDoubleOrNull() ?: 0.0
                        viewModel.setAmount(newAmountValue)
                    },
                    onClearClick = {
                        viewModel.setAmount(0.0)
                    },
                    onOkClick = {
                        coroutineScope.launch {
                            try {
                                isBottomSheetVisible = false
                                bottomSheetScaffoldState.bottomSheetState.hide()
                            } catch (e: Exception) {
                                Log.e("ItemAdd", "Error hiding BottomSheet: ${e.message}")
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
            }
        },
        sheetPeekHeight = if (isBottomSheetVisible) 50.dp else 0.dp // 控制 BottomSheet 的可見性
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFD9C9BA))  // 设置页面的背景颜色
                .padding(top = 65.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
                .clickable {
                    if (isBottomSheetVisible) {
                        coroutineScope.launch {
                            isBottomSheetVisible = false
                            bottomSheetScaffoldState.bottomSheetState.hide() // 隐藏 BottomSheet
                        }
                    }
                }
        ) {
            // 选项卡 (个人/群组)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { selectedTab = "個人" },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedTab == "個人") colorResource(id = R.color.colorAccent) else colorResource(id = R.color.primary_text_color)
                    )
                ) {
                    Text("個人")
                }
                Button(
                    onClick = { selectedTab = "群組" },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedTab == "群組") colorResource(id = R.color.colorAccent) else colorResource(id = R.color.primary_text_color)
                    )
                ) {
                    Text("群組")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 当前选项卡的内容
            if (selectedTab == "群組") {
                StylishTextField2(
                    value = groupName,
                    onValueChange = { groupName = it },
                    label = "群組名稱"
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // 收入/支出切换按钮
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "支出",
                    color = if (transactionType == "支出") colorResource(id = R.color.colorAccent) else Color.Gray,
                    fontSize = 18.sp
                )
                Switch(
                    checked = transactionType == "收入", // Switch is on if it's "收入"
                    onCheckedChange = { isChecked ->
                        viewModel.setTransactionType(if (isChecked) "收入" else "支出")
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = colorResource(id = R.color.colorAccent), // Color when checked
                        uncheckedThumbColor = colorResource(id = R.color.primary_text_color) // Color when unchecked
                    )
                )
                Text(
                    "收入",
                    color = if (transactionType == "收入") colorResource(id = R.color.colorAccent) else Color.Gray,
                    fontSize = 18.sp
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // 金額輸入
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("$", fontSize = 24.sp)
                Spacer(modifier = Modifier.width(8.dp))
                StylishTextField2(
                    value = amount.toString(),  // This is a TextFieldValue
                    onValueChange = { viewModel.setAmount(it.toDouble()) },
                    label = "金額",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            coroutineScope.launch {
                                isBottomSheetVisible = true
                                bottomSheetScaffoldState.bottomSheetState.expand()
                            }
                            keyboardController?.hide()
                        }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 類別、名稱
//            DropDownMenuField(
//                label = "類別",
//                selectedItem = category,
//                onItemSelected = { viewModel.setCategory(it) }
//            )
//            Spacer(modifier = Modifier.height(16.dp))
//            DropDownMenuField(
//                label = "名稱",
//                selectedItem = name,
//                onItemSelected = { viewModel.setName(it) }
//            )

            TransactionCategoryDropdown2(
                selectedCategory = selectedCategory,
                onCategorySelected = { category ->
                    if (isValidCategory(category)) {
                        selectedCategory = category
                    } else {
                        selectedCategory = TransactionCategory.OTHER
                    }
                }
            )

            // Name Input
            TextField(
                value = name,
                onValueChange = { viewModel.setName(it) },
                label = { Text("名稱") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))


            TextField(
                value = note,
                onValueChange = { viewModel.setNote(it) },
                label = { Text("Enter text") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (selectedTab == "群組") {
                Spacer(modifier = Modifier.height(16.dp))
                DropDownMenuField2(
                    label = "付款人",
                    selectedItem = payer,
                    onItemSelected = { payer = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 分账方式的三个按钮
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { splitMethod = "平均分攤" },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (splitMethod == "平均分攤") colorResource(id = R.color.colorAccent) else colorResource(id = R.color.primary_text_color)
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp)
                    ) {
                        Text("平均分攤")
                    }

                    Button(
                        onClick = { splitMethod = "填入金額" },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (splitMethod == "填入金額") colorResource(id = R.color.colorAccent) else colorResource(id = R.color.primary_text_color)
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp)
                    ) {
                        Text("填入金額")
                    }

                    Button(
                        onClick = { splitMethod = "填入份額" },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (splitMethod == "填入份額") colorResource(id = R.color.colorAccent) else colorResource(id = R.color.primary_text_color)
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp)
                    ) {
                        Text("填入份額")
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // 完成按钮
            Button(
                onClick = {
                    if (selectedTab == "個人") {
                        viewModel.addPersonalTransaction()
                        // 清空使用者填寫內容
                        viewModel.setAmount(0.0)
                        viewModel.setCategory(TransactionCategory.OTHER)
                        viewModel.setName("")
                        viewModel.setNote("")
                        // 回到上一頁
                        navController.popBackStack()
                    } else {
                        // 群组交易(還沒寫)
                    }
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

@Composable
fun TransactionCategoryDropdown2(
    selectedCategory: TransactionCategory,
    onCategorySelected: (TransactionCategory) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    // Display the selected category
    Text(
        text = selectedCategory.name,
        modifier = Modifier
            .clickable { expanded = true }
            .padding(16.dp)
    )

    // Dropdown menu
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        TransactionCategory.entries.forEach { category ->
            DropdownMenuItem(
                onClick = {
                    onCategorySelected(category)
                    expanded = false
                },
                text = {
                    Text(text = category.name)
                }
            )
        }
    }
}

fun isValidAmount(amount: String): Boolean {
    return amount.toDoubleOrNull() != null && amount.isNotEmpty()
}
fun isValidCategory(category: TransactionCategory): Boolean {
    return TransactionCategory.values().contains(category)
}

@Preview
@Composable
fun ItemAddPreview() {
    val navController = rememberNavController()
    val viewModel = MainViewModel()
    ItemAdd2(navController, viewModel)
}


