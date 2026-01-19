package com.example.expensetracker.ui

import com.example.expensetracker.data.ExpenseCategory
import com.example.expensetracker.data.local.ExpenseEntity

data class ExpenseUiState(
    val expenses: List<ExpenseEntity> = emptyList(),
    val selectedCategory: ExpenseCategory? = null
)