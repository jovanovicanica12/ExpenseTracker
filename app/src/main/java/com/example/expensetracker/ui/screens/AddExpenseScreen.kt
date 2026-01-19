package com.example.expensetracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.expensetracker.R
import com.example.expensetracker.data.ExpenseCategory
import com.example.expensetracker.ui.components.CategoryDropdown
import com.example.expensetracker.ui.components.ExpenseOutlinedTextField
import com.example.expensetracker.ui.components.PrimaryOrangeButton
import com.example.expensetracker.ui.theme.BlackColor
import com.example.expensetracker.ui.theme.OrangeColor
import com.example.expensetracker.ui.theme.WhiteColor
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    onBack: () -> Unit,
    onSave: (amount: Double, category: ExpenseCategory, desc: String) -> Unit
) {
    val keyboard = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()

    val snackbarHostState = remember { SnackbarHostState() }

    var amountText by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(ExpenseCategory.FOOD) }

    var showAmountError by remember { mutableStateOf(false) }
    var isSaving by remember { mutableStateOf(false) }

    val saveText = stringResource(R.string.save)
    val savingText = stringResource(R.string.saving)
    val expenseSavedText = stringResource(R.string.expense_saved)

    fun parseAmount(): Double? {
        val normalized = amountText.replace(",", ".").trim()
        val value = normalized.toDoubleOrNull()
        return if (value == null || value <= 0.0) null else value
    }

    val parsedAmount = remember(amountText) { parseAmount() }
    val canSave = parsedAmount != null && !isSaving

    Scaffold(
        containerColor = BlackColor,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.add_expense),
                        color = WhiteColor
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                            tint = WhiteColor
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BlackColor
                )
            )
        }
    ) { padding ->

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
                    isError = showAmountError && parsedAmount == null,
                    keyboardType = KeyboardType.Decimal,
                    trailing = { Text(text = "€", color = WhiteColor) }
                )

                if (showAmountError && parsedAmount == null) {
                    Text(
                        text = stringResource(R.string.enter_valid_amount),
                        color = OrangeColor,
                        style = MaterialTheme.typography.labelMedium
                    )
                }

                ExpenseOutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = stringResource(R.string.description),
                    keyboardType = KeyboardType.Text
                )

                CategoryDropdown(
                    selected = selectedCategory,
                    onSelected = { selectedCategory = it },
                    label = stringResource(R.string.category)
                )
            }

            PrimaryOrangeButton(
                text = if (isSaving) savingText else saveText,
                enabled = canSave,
                onClick = {
                    val amount = parseAmount()
                    if (amount == null) {
                        showAmountError = true
                        return@PrimaryOrangeButton
                    }

                    keyboard?.hide()
                    focusManager.clearFocus()

                    isSaving = true

                    onSave(amount, selectedCategory, description.trim())

                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = expenseSavedText,
                            withDismissAction = false,
                            duration = SnackbarDuration.Short
                        )
                        onBack()
                    }
                }
            )
        }
    }
}