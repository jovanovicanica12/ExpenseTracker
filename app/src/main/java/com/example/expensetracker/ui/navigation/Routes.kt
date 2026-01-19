package com.example.expensetracker.ui.navigation

sealed class Route(val path: String) {
    data object Home : Route("home")
    data object Add : Route("add")
    data object Stats : Route("stats")
    data object Edit : Route("edit/{id}") {
        fun create(id: Long) = "edit/$id"
    }
}