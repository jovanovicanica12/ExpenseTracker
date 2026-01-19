package com.example.expensetracker.data.local

import android.content.Context
import androidx.room.Room

object DbProvider {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun get(context: Context): AppDatabase =
        INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "expense_db"
            ).build().also { INSTANCE = it }
        }
}