package com.example.billapp.models

import java.security.Timestamp

data class Transaction(
    val transactionId: String = "",        // 交易記錄的唯一標識符
    val userId: String = "",               // 關聯的使用者 ID
    val type: String = "",                 // 交易類型 (支出, 收入)
    val amount: Double = 0.0,              // 交易金額
    val category: String = "",             // 交易分類
    val note: String? = null,              // 備註 (可選)
    val date: Timestamp? = null,           // 交易日期
    val createdAt: Timestamp? = null,      // 記錄創建的時間戳
    val updatedAt: Timestamp? = null       // 記錄更新的時間戳
)
