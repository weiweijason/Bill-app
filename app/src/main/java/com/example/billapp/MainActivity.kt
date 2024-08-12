package com.example.billapp.ui.theme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.res.colorResource
import com.example.billapp.R
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BillAppTheme {
                BillAppScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StylishTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    label: String
) {
    val capybaraBrown = colorResource(id = R.color.colorAccent)

    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        textStyle = TextStyle(
            fontSize = 18.sp,
            fontFamily = FontFamily.Cursive,  // 酷酷字體 待改
            color = capybaraBrown
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(2.dp, capybaraBrown, RoundedCornerShape(8.dp)),  // 加圓角
        colors = TextFieldDefaults.colors(
            focusedContainerColor = colorResource(id = R.color.colorLight), // 聚焦时的背景色
            unfocusedContainerColor = colorResource(id = R.color.colorLight), // 未聚焦時的背景色
            focusedIndicatorColor = capybaraBrown, // 聚焦時
            unfocusedIndicatorColor = Color.Transparent // 未聚焦時
        )
    )
}

@Composable
fun BillAppScreen() {
    var selectedTab by remember { mutableStateOf("個人") }
    var groupName by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf(TextFieldValue("")) }
    var category by remember { mutableStateOf(TextFieldValue("")) }
    var name by remember { mutableStateOf(TextFieldValue("")) }
    var payer by remember { mutableStateOf(TextFieldValue("")) }
    var splitMethod by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD9C9BA))  // 設置背景顏色
            .padding(16.dp)  // 背景padding
    ) {
        // (個人/群組)
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

        // 當前(個人/群組)的內容
        if (selectedTab == "群組") {
            StylishTextField(
                value = TextFieldValue(groupName),
                onValueChange = { groupName = it.text },
                label = "群組名稱"
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
            StylishTextField(
                value = amount,
                onValueChange = { amount = it },
                label = "金額"
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 類別、名稱、付款人、分帳方式
        StylishTextField(
            value = category,
            onValueChange = { category = it },
            label = "類別"
        )

        Spacer(modifier = Modifier.height(16.dp))

        StylishTextField(
            value = name,
            onValueChange = { name = it },
            label = "名稱"
        )

        Spacer(modifier = Modifier.height(16.dp))

        StylishTextField(
            value = payer,
            onValueChange = { payer = it },
            label = "付款人"
        )

        Spacer(modifier = Modifier.height(16.dp))

        StylishTextField(
            value = splitMethod,
            onValueChange = { splitMethod = it },
            label = "分帳方式"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BillAppTheme {
        BillAppScreen()
    }
}
