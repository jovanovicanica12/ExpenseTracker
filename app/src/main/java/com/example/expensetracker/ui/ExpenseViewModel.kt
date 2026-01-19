package com.example.expensetracker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.data.ExpenseCategory
import com.example.expensetracker.data.ExpenseRepository
import com.example.expensetracker.data.local.ExpenseEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ExpenseViewModel(
    private val repo: ExpenseRepository
) : ViewModel() {

    private val selectedCategory = MutableStateFlow<ExpenseCategory?>(null)

    val allExpenses: StateFlow<List<ExpenseEntity>> =
        repo.observeExpenses()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyList()
            )

    val uiState: StateFlow<ExpenseUiState> =
        combine(allExpenses, selectedCategory) { expenses, category ->
            val filtered =
                if (category == null) expenses else expenses.filter { it.category == category }
            ExpenseUiState(expenses = filtered, selectedCategory = category)
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            ExpenseUiState()
        )

    fun setCategory(category: ExpenseCategory?) {
        selectedCategory.value = category
    }

    fun addExpense(amount: Double, category: ExpenseCategory, description: String) {
        viewModelScope.launch {
            repo.addExpense(amount = amount, category = category, description = description)
        }
    }

    fun deleteExpense(expense: ExpenseEntity) {
        viewModelScope.launch { repo.deleteExpense(expense) }
    }

    fun observeExpenseById(id: Long): Flow<ExpenseEntity?> = repo.observeExpenseById(id)

    fun updateExpense(
        id: Long,
        amount: Double,
        category: ExpenseCategory,
        description: String
    ) {
        viewModelScope.launch {
            repo.updateExpense(
                id = id,
                amount = amount,
                category = category,
                description = description
            )
        }
    }
}