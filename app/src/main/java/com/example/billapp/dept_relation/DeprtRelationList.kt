package com.example.billapp.dept_relation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.billapp.models.DeptRelation
import com.example.billapp.viewModel.MainViewModel

@Composable
fun DeptRelationList(
    viewModel: MainViewModel,
    deptRelations: Map<String, List<DeptRelation>>,
    groupId: String
) {
    val groupedDeptRelations = deptRelations.values.flatten().groupBy { Pair(it.from, it.to) }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        groupedDeptRelations.forEach { (pair, relations) ->
            item {
                var fromName by remember { mutableStateOf("") }
                var toName by remember { mutableStateOf("") }

                LaunchedEffect(pair.first, pair.second) {
                    fromName = viewModel.getUserName(pair.first)
                    toName = viewModel.getUserName(pair.second)
                }

                GroupedDeptRelationItem(
                    viewModel = viewModel,
                    fromName = fromName,
                    toName = toName,
                    totalAmount = relations.sumOf { it.amount },
                    deptRelations = relations,
                    groupId = groupId
                )
            }
        }
    }
}