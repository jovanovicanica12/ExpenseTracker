package com.example.expensetracker.data

import com.example.expensetracker.data.local.ExpenseDao
import com.example.expensetracker.data.local.ExpenseEntity
import kotlinx.coroutines.flow.Flow

class ExpenseRepository(private val dao: ExpenseDao) {

    fun observeExpenses(): Flow<List<ExpenseEntity>> = dao.observeAll()

    fun observeExpenseById(id: Long): Flow<ExpenseEntity?> = dao.observeById(id)

    suspend fun addExpense(
        amount: Double,
        category: ExpenseCategory,
        description: String
    ) {
        dao.insert(
            ExpenseEntity(
                amount = amount,
                category = category,
                description = description,
                createdAtMillis = System.currentTimeMillis()
            )
        )
    }

    suspend fun updateExpense(
        id: Long,
        amount: Double,
        category: ExpenseCategory,
        description: String
    ) {
        dao.updateById(
            id = id,
            amount = amount,
            category = category.name,
            description = description
        )
    }

    suspend fun deleteExpense(expense: ExpenseEntity) {
        dao.delete(expense)
    }
}
