package com.example.expensetracker.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = OrangeColor,
    secondary = OrangeColor,
    background = BlackColor,
    surface = GrayColor,
    onPrimary = WhiteColor,
    onSecondary = WhiteColor,
    onBackground = WhiteColor,
    onSurface = WhiteColor
)

@Composable
fun ExpenseTrackerTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
