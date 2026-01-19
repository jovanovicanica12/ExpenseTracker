package com.example.expensetracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.example.expensetracker.R
import com.example.expensetracker.data.ExpenseCategory
import com.example.expensetracker.data.local.ExpenseEntity
import com.example.expensetracker.ui.ExpenseViewModel
import com.example.expensetracker.ui.components.CategoryDropdown
import com.example.expensetracker.ui.components.ExpenseOutlinedTextField
import com.example.expensetracker.ui.components.PrimaryOrangeButton
import com.example.expensetracker.ui.theme.BlackColor
import com.example.expensetracker.ui.theme.OrangeColor
import com.example.expensetracker.ui.theme.WhiteColor
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditExpenseScreen(
    vm: ExpenseViewModel,
    expenseId: Long,
    onBack: () -> Unit
) {
    val keyboard = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()

    val snackbarHostState = remember { SnackbarHostState() }
    val expenseUpdatedText = stringResource(R.string.expense_updated)

    var expense by remember { mutableStateOf<ExpenseEntity?>(null) }

    LaunchedEffect(expenseId) {
        vm.observeExpenseById(expenseId).collectLatest { expense = it }
    }

    var amountText by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(ExpenseCategory.FOOD) }
    var showAmountError by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(expense) {
        expense?.let {
            amountText = it.amount.toString()
            description = it.description
            selectedCategory = it.category
        }
    }

    fun parseAmount(): Double? {
        val value = amountText.replace(",", ".").toDoubleOrNull()
        return if (value != null && value > 0) value else null
    }

    Scaffold(
        containerColor = BlackColor,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.edit_expense), color = WhiteColor) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = null, tint = WhiteColor)
                    }
                },
                actions = {
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Filled.Delete, contentDescription = null, tint = OrangeColor)
                    }
                }
            )
        }
    ) { padding ->

        if (expense == null) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(stringResource(R.string.expense_not_found), color = WhiteColor)
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {

                ExpenseOutlinedTextField(
                    value = amountText,
                    onValueChange = {
                        amountText = it
                        showAmountError = false
                    },
                    label = stringResource(R.string.amount),
                    singleLine = true,
                    isError = showAmountError,
                    keyboardType = KeyboardType.Decimal,
                    trailing = { Text(stringResource(R.string.euro), color = WhiteColor) }
                )

                if (showAmountError) {
                    Text(
                        stringResource(R.string.enter_valid_amount),
                        color = OrangeColor
                    )
                }

                ExpenseOutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = stringResource(R.string.description)
                )

                CategoryDropdown(
                    selected = selectedCategory,
                    onSelected = { selectedCategory = it },
                    label = stringResource(R.string.category)
                )
            }

            PrimaryOrangeButton(
                text = stringResource(R.string.update),
                onClick = {
                    val amount = parseAmount()
                    if (amount == null) {
                        showAmountError = true
                        return@PrimaryOrangeButton
                    }

                    keyboard?.hide()
                    focusManager.clearFocus()

                    vm.updateExpense(
                        id = expenseId,
                        amount = amount,
                        category = selectedCategory,
                        description = description.trim()
                    )

                    scope.launch {
                        snackbarHostState.showSnackbar(expenseUpdatedText)
                        onBack()
                    }
                }
            )
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(R.string.delete_expense_title)) },
            text = { Text(stringResource(R.string.delete_expense_message)) },
            confirmButton = {
                TextButton(onClick = {
                    expense?.let { vm.deleteExpense(it) }
                    onBack()
                }) {
                    Text(stringResource(R.string.delete), color = OrangeColor)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}