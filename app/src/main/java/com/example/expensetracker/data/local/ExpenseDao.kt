package com.example.expensetracker.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Query("SELECT * FROM expenses ORDER BY createdAtMillis DESC")
    fun observeAll(): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expenses WHERE id = :id LIMIT 1")
    fun observeById(id: Long): Flow<ExpenseEntity?>

    @Insert
    suspend fun insert(expense: ExpenseEntity)

    @Query(
        """
        UPDATE expenses
        SET amount = :amount,
            category = :category,
            description = :description
        WHERE id = :id
    """
    )
    suspend fun updateById(
        id: Long,
        amount: Double,
        category: String,
        description: String
    )

    @Delete
    suspend fun delete(expense: ExpenseEntity)

    @Query("DELETE FROM expenses")
    suspend fun clearAll()
}