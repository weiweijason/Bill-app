package com.example.billapp.models

data class Group(
    val name: String,
    val image: String,
    val usersID: List<GroupTransaction>,  // List of user id
    val transactions: List<GroupTransaction>,
    val deptRelations: List<DeptRelation>
)
