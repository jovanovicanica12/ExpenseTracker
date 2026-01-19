package com.example.expensetracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.expensetracker.R
import com.example.expensetracker.data.local.ExpenseEntity
import com.example.expensetracker.ui.ExpenseViewModel
import com.example.expensetracker.ui.components.CategoryFilterRow
import com.example.expensetracker.ui.components.ExpenseRow
import com.example.expensetracker.ui.components.PrimaryOrangeButton
import com.example.expensetracker.ui.components.SummaryCards
import com.example.expensetracker.ui.theme.BlackColor
import com.example.expensetracker.ui.theme.OrangeColor
import com.example.expensetracker.ui.theme.WhiteColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    vm: ExpenseViewModel,
    onAdd: () -> Unit,
    onStats: () -> Unit,
    onEdit: (Long) -> Unit
) {
    val state by vm.uiState.collectAsState()
    var toDelete by remember { mutableStateOf<ExpenseEntity?>(null) }

    Scaffold(
        containerColor = BlackColor,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(R.string.app_name), color = WhiteColor) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = BlackColor
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAdd,
                containerColor = OrangeColor,
                contentColor = WhiteColor
            ) {
                Text(text = stringResource(R.string.plus))
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            SummaryCards(
                total = state.expenses.sumOf { it.amount },
                count = state.expenses.size
            )

            PrimaryOrangeButton(
                text = stringResource(R.string.stats),
                onClick = onStats
            )

            Spacer(Modifier.height(12.dp))

            CategoryFilterRow(
                selected = state.selectedCategory,
                onSelect = { vm.setCategory(it) }
            )

            Spacer(Modifier.height(12.dp))

            if (state.expenses.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.no_expenses),
                        color = WhiteColor
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 96.dp)
                ) {
                    items(state.expenses) { expense ->
                        ExpenseRow(
                            expense = expense,
                            onClick = { onEdit(expense.id) },
                            onDelete = { toDelete = it }
                        )
                    }
                }
            }
        }
    }

    toDelete?.let { expense ->
        AlertDialog(
            onDismissRequest = { toDelete = null },
            title = { Text(text = stringResource(R.string.delete_expensive)) },
            text = { Text(text = stringResource(R.string.message)) },
            confirmButton = {
                TextButton(onClick = {
                    vm.deleteExpense(expense)
                    toDelete = null
                }) {
                    Text(text = stringResource(R.string.delete))
                }
            },
            dismissButton = {
                TextButton(onClick = { toDelete = null }) {
                    Text(text = stringResource(R.string.cancel))
                }
            }
        )
    }
}