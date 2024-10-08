package com.example.billapp

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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import com.example.billapp.models.Group
import com.example.billapp.models.TransactionCategory
import com.example.billapp.models.User
import com.example.billapp.viewModel.MainViewModel
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch
import androidx.compose.animation.AnimatedVisibility


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StylishTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    readOnly: Boolean,
    modifier: Modifier = Modifier
) {
    val capybaraBrown = colorResource(id = R.color.colorAccent)

    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        readOnly = readOnly,
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
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),

    )
}


@Composable
fun CustomKeyboard(
    onKeyClick: (String) -> Unit,
    onDeleteClick: () -> Unit,
    onClearClick: () -> Unit,
    onOkClick: () -> Unit,
    onEqualsClick: () -> Unit,
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
            Button(modifier = buttonModifier, onClick = onEqualsClick, colors = buttonColors) {
                Text("=", color = capybaraBrown)
            }
            Button(modifier = buttonModifier, onClick = onOkClick, colors = buttonColors) {
                Text("OK", color = capybaraBrown)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemAdd(
    navController: NavController,
    viewModel: MainViewModel
) {
    val transactionType by viewModel.transactionType.collectAsState()
    var personalOrGroup by remember { mutableStateOf("個人") }
    val type by viewModel.transactionType.collectAsState()
    val amount by viewModel.amount.collectAsState()
    var amountInput by remember { mutableStateOf(amount.toString()) }
    val name by viewModel.name.collectAsState()
    val categories = TransactionCategory.entries.toTypedArray()
//    val category by viewModel.category.collectAsState()
//    var selectedCategory by remember { mutableStateOf(TransactionCategory.OTHER) }
    val selectedCategory by viewModel.category.collectAsState()
    val note by viewModel.note.collectAsState()
//    var groupName by remember { mutableStateOf("") }
    var splitMethod by remember { mutableStateOf("平均分攤") } // default 平攤
    var expandedGroup by remember { mutableStateOf(false) }
    var expandedCategory by remember { mutableStateOf(false) }
    var expandedPayers by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()
    val keyboardController = LocalSoftwareKeyboardController.current

    var isBottomSheetVisible by remember { mutableStateOf(false) }
    var isKeyboardVisible by remember { mutableStateOf(false) }
    var toggleKeyboard by remember { mutableStateOf(false) }

    //Group
    val groups by viewModel.userGroups.collectAsState()
    var selectedGroup by remember { mutableStateOf(groups.firstOrNull()?.name ?: "groups") }
    val shareMethod by viewModel.shareMethod.collectAsState()
    val dividers by viewModel.dividers.collectAsState()
    val payers by viewModel.payers.collectAsState()
    val groupMembers by viewModel.groupMembers.collectAsState()

    var expandedShareMethod by remember { mutableStateOf(false) }
    var expandedDividers by remember { mutableStateOf(false) }


    // Helper to get a user's name by ID
    val getUserNameById: (String) -> String = { userId ->
        groupMembers.find { it.id == userId }?.name ?: "Unknown"
    }

    // Initialize the category to ensure it has a default value
    LaunchedEffect(Unit) {
        viewModel.loadUserGroups()
        viewModel.reloadUserData()
    }
    LaunchedEffect(Unit) {
        viewModel.setCategory(TransactionCategory.OTHER)
    }


    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {},
        sheetPeekHeight = 0.dp,

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFD9C9BA))  // 背景顏色
                .padding(top = 23.dp, start = 16.dp, end = 16.dp, bottom = 7.dp)
                .clickable {
                    if (isBottomSheetVisible) {
                        coroutineScope.launch {
                            isBottomSheetVisible = false
                            toggleKeyboard = false
                        }
                    }
                }
        ) {
            // 主要程式碼
            // (個人/群组)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { personalOrGroup = "個人" },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (personalOrGroup == "個人") colorResource(id = R.color.colorAccent) else colorResource(id = R.color.primary_text_color)
                    )
                ) {
                    Text("個人")
                }
                Button(
                    onClick = { personalOrGroup = "群組" },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (personalOrGroup == "群組") colorResource(id = R.color.colorAccent) else colorResource(id = R.color.primary_text_color)
                    )
                ) {
                    Text("群組")
                }
            }

            Spacer(modifier = Modifier.height(7.dp))

            // if群組
            if (personalOrGroup == "群組") {
                ExposedDropdownMenuBox(
                    expanded = expandedGroup,
                    onExpandedChange = { expandedGroup = !expandedGroup }  // 使用 onExpandedChange 處理展開/收起狀態
                ) {
                    StylishTextField(
                        value = selectedGroup,
                        onValueChange = { selectedGroup = it },
                        label = "選擇群組",
                        readOnly = true,
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth() // 移除 clickable 修飾符
                    )
                    ExposedDropdownMenu(
                        expanded = expandedGroup,
                        onDismissRequest = { expandedGroup = false }
                    ) {
                        groups.forEach { group ->
                            DropdownMenuItem(
                                text = { Text(text = group.name) },
                                onClick = {
                                    selectedGroup = group.name
                                    expandedGroup = false
                                }
                            )
                        }
                    }
                }
            }
            else{
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { viewModel.setTransactionType("支出") },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (transactionType == "支出") colorResource(id = R.color.colorAccent) else colorResource(
                                id = R.color.primary_text_color
                            )
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "支出")
                    }

                    Spacer(modifier = Modifier.width(7.dp))

                    Button(
                        onClick = { viewModel.setTransactionType("收入") },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (transactionType == "收入") colorResource(id = R.color.colorAccent) else colorResource(
                                id = R.color.primary_text_color
                            )
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "收入")
                    }
                }
            }


            // 金額輸入
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                StylishTextField(
                    value = amountInput,
                    onValueChange = {
                        amountInput = it
                        it.toDoubleOrNull()?.let { validAmount ->
                            viewModel.setAmount(validAmount)
                        }
                    },
                    label = "金額",
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable {toggleKeyboard = !toggleKeyboard
                            isBottomSheetVisible = toggleKeyboard }
                )

            }

            AnimatedVisibility(visible = isBottomSheetVisible) {
                CustomKeyboard(
                    onKeyClick = { key ->
                        amountInput += key
                        // 驗證並更新金額輸入
                        amountInput.toDoubleOrNull()?.let { validAmount ->
                            viewModel.setAmount(validAmount)
                        }
                    },
                    onDeleteClick = {
                        if (amountInput.isNotEmpty()) {
                            amountInput = amountInput.dropLast(1)
                            amountInput.toDoubleOrNull()?.let { validAmount ->
                                viewModel.setAmount(validAmount)
                            }
                        }
                    },
                    onClearClick = {
                        amountInput = ""
                        viewModel.setAmount(0.0)
                    },
                    onOkClick = {
                        coroutineScope.launch {
                            isBottomSheetVisible = false
                            toggleKeyboard = false
                        }
                    },
                    onEqualsClick = {
                        // 計算 amountInput
                        val result = evaluateExpression(amountInput)
                        amountInput = result.toString()
                        viewModel.setAmount(result)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(7.dp))

            // 名稱
            StylishTextField(
                value = name,
                onValueChange = { viewModel.setName(it) },
                label = "Name",
                readOnly = false,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(7.dp))


            //類別
            ExposedDropdownMenuBox(
                expanded = expandedCategory,
                onExpandedChange = { expandedCategory = !expandedCategory }
            ) {
                StylishTextField(
                    value = selectedCategory,
                    onValueChange = {},
                    label = "選擇類別" ,
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    modifier = Modifier.exposedDropdownSize(matchTextFieldWidth = true),
                    expanded = expandedCategory,
                    onDismissRequest = { expandedCategory = false }
                ) {
                    categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category.name) },
                            onClick = {
                                viewModel.setCategory(category)
                                expandedCategory = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(7.dp))


            if (personalOrGroup == "群組") {

                ExposedDropdownMenuBox(
                    expanded = expandedPayers,
                    onExpandedChange = { expandedPayers = !expandedPayers }
                ) {
                    StylishTextField(
                        readOnly = true,
                        value = payers.joinToString(", ") { getUserNameById(it) },
                        onValueChange = { },
                        label = "付款人",
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedPayers,
                        onDismissRequest = { expandedPayers = false }
                    ) {
                        groupMembers.forEach { user ->
                            DropdownMenuItem(
                                text = { Text(user.name) },
                                onClick = {
                                    viewModel.togglePayer(user.id)
                                    expandedPayers = false
                                }
                            )
                        }
                    }
                }

                // 分帳三個按鈕
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

            Spacer(modifier = Modifier.height(5.dp))

            //備註
            StylishTextField(
                value = note,
                onValueChange = { viewModel.setNote(it) },
                label = "Note",
                readOnly = false,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            // 完成按鈕
            if(personalOrGroup == "群組"){
                Button(
                    onClick = {
                    },
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(16.dp)
                ) {
                    Text("完成")
                }
            }else{
                Button(
                    onClick = {
                        viewModel.addPersonalTransaction()
                        amountInput=""
                        viewModel.setAmount(0.0)
                        viewModel.setCategory(TransactionCategory.OTHER)
                        viewModel.setName("")
                        viewModel.setNote("")
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


// 計算函式
// 簡單版
fun evaluateExpression(expression: String): Double {
    return try {
        val result = expression.split("+", "-", "×", "÷")
            .map { it.trim().toDoubleOrNull() ?: 0.0 }
            .reduce { acc, value -> acc + value }
        result
    } catch (e: Exception) {
        0.0
    }
}

