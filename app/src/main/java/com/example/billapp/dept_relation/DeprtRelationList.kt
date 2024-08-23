package com.example.billapp.dept_relation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.billapp.models.DeptRelation

@Composable
fun DeptRelationList(deptRelations: List<DeptRelation>) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(deptRelations) { deptRelation ->
            DeptRelationItem(deptRelation = deptRelation)
        }
    }
}