package com.example.billapp.models

import android.os.Parcelable

data class Group(
    val name: String,
    val image: String,
    val createdBy: String,
    val usersID: List<GroupTransaction>,  // List of user id
    val transactions: List<GroupTransaction>,
    val deptRelations: List<DeptRelation>
)