package com.example.expensetracker.ui.util

import java.util.Locale

fun formatMoney(amount: Double): String {
    return String.format(Locale.US, "%.2f %s", amount, CURRENCY_SYMBOL)
}