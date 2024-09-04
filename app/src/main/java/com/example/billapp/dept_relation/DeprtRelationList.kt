package com.example.billapp.dept_relation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.billapp.models.DeptRelation
import com.example.billapp.viewModel.MainViewModel

@Composable
fun DeptRelationList(
    deptRelations: Map<String, List<DeptRelation>>,
    groupId : String
) {
    // 優化後
    val groupedDeptRelations = deptRelations.values.flatten().groupBy { Pair(it.from, it.to) }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        groupedDeptRelations.forEach { (pair, relations) ->
            item {
                GroupedDeptRelationItem(
                    viewModel = MainViewModel(),
                    from = pair.first,
                    to = pair.second,
                    totalAmount = relations.sumOf { it.amount },
                    deptRelations = relations,
                    groupId = groupId
                )
            }
        }
    }
}