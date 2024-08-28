package com.example.billapp

import android.net.Uri
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.billapp.PieChart
import com.example.billapp.PieChartWithCategory
import com.example.billapp.viewModel.MainViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun YearPickerDialog(
    onYearSelected: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    var selectedYear by remember { mutableStateOf(currentYear) }

    // LazyListState for controlling the scroll position
    val yearListState = rememberLazyListState()

    // Scroll to the current year when the composable is first displayed
    LaunchedEffect(Unit) {
        yearListState.scrollToItem(currentYear - 2010)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onYearSelected(selectedYear)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("選擇年份", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn(
                    state = yearListState,
                    modifier = Modifier.weight(1f).height(200.dp) // 限制高度以便滾動
                ) {
                    items((2010..currentYear).toList()) { year ->
                        Text(
                            text = year.toString(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedYear = year
                                }
                                .padding(16.dp),
                            textAlign = TextAlign.Center,
                            fontWeight = if (year == selectedYear) FontWeight.Bold else FontWeight.Normal,
                            color = if (year == selectedYear) Color.Blue else Color.Black
                        )
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthPickerDialog(
    onMonthSelected: (Int, Int) -> Unit,
    onDismiss: () -> Unit
) {
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
    var selectedYear by remember { mutableStateOf(currentYear) }
    var selectedMonth by remember { mutableStateOf(currentMonth) }

    // LazyListState for controlling the scroll position
    val yearListState = rememberLazyListState()
    val monthListState = rememberLazyListState()

    // Scroll to the current year and month when the composable is first displayed
    LaunchedEffect(Unit) {
        yearListState.scrollToItem(currentYear - 2010)
        monthListState.scrollToItem(currentMonth)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onMonthSelected(selectedYear, selectedMonth)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("選擇年份和月份", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))

                Row {
                    // Year Picker
                    LazyColumn(
                        state = yearListState,
                        modifier = Modifier.weight(1f).height(200.dp) // 限制高度以便滾動
                    ) {
                        items((2010..currentYear).toList()) { year ->
                            Text(
                                text = year.toString(),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedYear = year
                                    }
                                    .padding(16.dp),
                                textAlign = TextAlign.Center,
                                fontWeight = if (year == selectedYear) FontWeight.Bold else FontWeight.Normal,
                                color = if (year == selectedYear) Color.Blue else Color.Black
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Month Picker
                    LazyColumn(
                        state = monthListState,
                        modifier = Modifier.weight(1f).height(200.dp) // 限制高度以便滾動
                    ) {
                        items((0..11).toList()) { month ->
                            Text(
                                text = (month + 1).toString(),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedMonth = month
                                    }
                                    .padding(16.dp),
                                textAlign = TextAlign.Center,
                                fontWeight = if (month == selectedMonth) FontWeight.Bold else FontWeight.Normal,
                                color = if (month == selectedMonth) Color.Blue else Color.Black
                            )
                        }
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDatePickerDialog(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        content = {
            DatePicker(state = datePickerState)
        }
    )
}