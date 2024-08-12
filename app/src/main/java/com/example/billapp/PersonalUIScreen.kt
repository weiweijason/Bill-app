package com.example.billapp

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.*
import kotlin.math.absoluteValue

data class FinanceRecord(
    val date: String,   // 记录的日期
    val amount: Float,  // 记录的金额（正值代表收入，负值代表支出）
    val note: String    // 记录的备注
)

@Composable
fun PersonalUIScreen() {
    var year by remember { mutableStateOf(Calendar.getInstance().get(Calendar.YEAR)) }
    var month by remember { mutableStateOf(Calendar.getInstance().get(Calendar.MONTH) + 1) }

    // 模拟三个月的收入与支出数据
    val financeData = mapOf(
        Pair(2024, 5) to listOf(
            FinanceRecord("2024-05-01", -200f, "买衣服"),
            FinanceRecord("2024-05-03", -50f, "买咖啡"),
            FinanceRecord("2024-05-10", 5000f, "工资"),
            FinanceRecord("2024-05-15", -300f, "买鞋"),
            FinanceRecord("2024-05-20", -100f, "超市购物")
        ),
        Pair(2024, 6) to listOf(
            FinanceRecord("2024-06-01", 5500f, "工资"),
            FinanceRecord("2024-06-05", -150f, "餐厅吃饭"),
            FinanceRecord("2024-06-10", -200f, "电影院"),
            FinanceRecord("2024-06-20", -300f, "买书"),
            FinanceRecord("2024-06-25", -250f, "旅行")
        ),
        Pair(2024, 7) to listOf(
            FinanceRecord("2024-07-01", 5200f, "工资"),
            FinanceRecord("2024-07-05", -100f, "健身房"),
            FinanceRecord("2024-07-12", -200f, "朋友聚会"),
            FinanceRecord("2024-07-15", -150f, "买礼物"),
            FinanceRecord("2024-07-20", -300f, "家居用品")
        )
    )

    var selectedChart by remember { mutableStateOf("支出") }
    var records by remember { mutableStateOf(financeData[Pair(year, month)] ?: emptyList()) }
    var filteredRecords by remember { mutableStateOf(records) }

    // 根据选中的类型过滤记录
    fun filterRecords() {
        filteredRecords = when (selectedChart) {
            "支出" -> records.filter { it.amount < 0 }
            "收入" -> records.filter { it.amount > 0 }
            "结余" -> records
            else -> records
        }
    }

    // 更新到下一个月或上一个月
    fun updateMonth(increment: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, 1)
        calendar.add(Calendar.MONTH, increment)
        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH) + 1

        // 更新数据
        records = financeData[Pair(year, month)] ?: emptyList()
        filterRecords()  // 过滤记录
    }

    // 切换数据类型（支出、收入或结余）
    fun switchData(type: String) {
        selectedChart = type
        filterRecords()
    }

    // 获取显示金额
    fun getDisplayAmount(): Float {
        return filteredRecords.sumOf { it.amount.toDouble() }.toFloat()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 显示年月的Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { updateMonth(-1) }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "上一个月"
                )
            }

            Text(
                text = "${year}年${month}月",
                fontSize = 24.sp,
                modifier = Modifier.align(Alignment.CenterVertically)
            )

            IconButton(onClick = { updateMonth(1) }) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "下一个月"
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { switchData("支出") }) {
                Text(text = "月支出")
            }

            Button(onClick = { switchData("收入") }) {
                Text(text = "月收入")
            }

            Button(onClick = { switchData("结余") }) {
                Text(text = "月结余")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "当前金额: ${getDisplayAmount()}",
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(32.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .align(Alignment.CenterHorizontally)
        ) {
            PieChart(
                modifier = Modifier
                    .size(200.dp)
                    .padding(16.dp),
                data = filteredRecords,
                displayAmount = getDisplayAmount()
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            Text(text = "收入和支出详情", fontSize = 20.sp, modifier = Modifier.padding(bottom = 16.dp))

            filteredRecords.forEach { record ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = record.date, modifier = Modifier.weight(1f))
                    Text(text = "${record.amount}", modifier = Modifier.weight(1f))
                    Text(text = record.note, modifier = Modifier.weight(2f))
                }
                Divider(color = Color.Gray, thickness = 1.dp)
            }
        }
    }
}

@Composable
fun PieChart(modifier: Modifier = Modifier, data: List<FinanceRecord>, displayAmount: Float) {
    Canvas(modifier = modifier) {
        var totalIncome = 0f
        var totalSpend = 0f

        for (record in data) {
            if (record.amount > 0) {
                totalIncome += record.amount
            } else {
                totalSpend += record.amount.absoluteValue
            }
        }

        val total = totalIncome + totalSpend
        val startAngle = -90f

        val spendFraction = if (total > 0) totalSpend / total else 0f
        val incomeFraction = if (total > 0) totalIncome / total else 0f

        drawArc(
            color = Color.Red,
            startAngle = startAngle,
            sweepAngle = spendFraction * 360f,
            useCenter = true,
            size = size,
            style = Stroke(width = 20.dp.toPx(), cap = StrokeCap.Round)
        )

        drawArc(
            color = Color.Green,
            startAngle = startAngle + spendFraction * 360f,
            sweepAngle = incomeFraction * 360f,
            useCenter = true,
            size = size,
            style = Stroke(width = 20.dp.toPx(), cap = StrokeCap.Round)
        )

        drawContext.canvas.nativeCanvas.apply {
            drawText(
                "$displayAmount",
                size.width / 2,
                size.height / 2,
                Paint().apply {
                    color = android.graphics.Color.BLACK
                    textAlign = Paint.Align.CENTER
                    textSize = 40.sp.toPx()
                }
            )
        }
    }
}
