package com.example.expensetracker.data.local

import androidx.room.TypeConverter
import com.example.expensetracker.data.ExpenseCategory

class ExpenseConverters {

    @TypeConverter
    fun toCategory(value: String): ExpenseCategory = ExpenseCategory.valueOf(value)

    @TypeConverter
    fun fromCategory(category: ExpenseCategory): String = category.name
}