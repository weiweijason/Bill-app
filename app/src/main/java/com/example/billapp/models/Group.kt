package com.example.billapp.models

data class Group(
    val name: String,
    val transactions: List<GroupTransaction>
)
