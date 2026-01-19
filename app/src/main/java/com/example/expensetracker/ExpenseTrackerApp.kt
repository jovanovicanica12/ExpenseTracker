package com.example.expensetracker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.expensetracker.data.ExpenseRepository
import com.example.expensetracker.data.local.DbProvider
import com.example.expensetracker.ui.ExpenseViewModel
import com.example.expensetracker.ui.ExpenseVmFactory
import com.example.expensetracker.ui.navigation.Route
import com.example.expensetracker.ui.screens.AddExpenseScreen
import com.example.expensetracker.ui.screens.EditExpenseScreen
import com.example.expensetracker.ui.screens.HomeScreen
import com.example.expensetracker.ui.screens.StatsScreen

@Composable
fun ExpenseTrackerApp() {
    val context = LocalContext.current
    val db = remember { DbProvider.get(context) }
    val repo = remember { ExpenseRepository(db.expenseDao()) }

    val vm: ExpenseViewModel = viewModel(factory = ExpenseVmFactory(repo))
    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = Route.Home.path) {

        composable(Route.Home.path) {
            HomeScreen(
                vm = vm,
                onAdd = { nav.navigate(Route.Add.path) },
                onStats = { nav.navigate(Route.Stats.path) },
                onEdit = { id -> nav.navigate(Route.Edit.create(id)) }
            )
        }

        composable(Route.Add.path) {
            AddExpenseScreen(
                onBack = { nav.popBackStack() },
                onSave = { amount, category, desc ->
                    vm.addExpense(amount, category, desc)
                }
            )
        }

        composable(Route.Stats.path) {
            StatsScreen(
                vm = vm,
                onBack = { nav.popBackStack() }
            )
        }

        composable(Route.Edit.path) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toLongOrNull()
            if (id == null) {
                nav.popBackStack()
                return@composable
            }

            EditExpenseScreen(
                vm = vm,
                expenseId = id,
                onBack = { nav.popBackStack() }
            )
        }
    }
}