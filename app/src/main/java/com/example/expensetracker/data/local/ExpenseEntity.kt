package com.example.expensetracker.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.expensetracker.data.ExpenseCategory

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val amount: Double,
    val category: ExpenseCategory,
    val description: String,
    val createdAtMillis: Long
)