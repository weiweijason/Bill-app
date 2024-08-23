package com.example.billapp.dept_relation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.billapp.viewModel.MainViewModel

@Composable
fun DeptRelationsScreen(viewModel: MainViewModel, groupId: String) {
    val deptRelations by viewModel.deptRelations.collectAsState()

    // Load transactions and calculate dept relations when the screen is opened
    LaunchedEffect(groupId) {
        viewModel.loadGroupTransactions(groupId)
    }
    DeptRelationList(deptRelations = deptRelations)
}