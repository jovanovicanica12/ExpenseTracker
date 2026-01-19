package com.example.expensetracker.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.example.expensetracker.ui.theme.GrayColor
import com.example.expensetracker.ui.theme.OrangeColor
import com.example.expensetracker.ui.theme.WhiteColor

@Composable
fun ExpenseOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean = false,
    isError: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    readOnly: Boolean = false,
    trailing: (@Composable (() -> Unit))? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        label = { Text(text = label) },
        singleLine = singleLine,
        isError = isError,
        readOnly = readOnly,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        trailingIcon = trailing,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = GrayColor,
            unfocusedContainerColor = GrayColor,
            disabledContainerColor = GrayColor,
            errorContainerColor = GrayColor,

            focusedTextColor = WhiteColor,
            unfocusedTextColor = WhiteColor,
            errorTextColor = WhiteColor,

            focusedBorderColor = OrangeColor,
            unfocusedBorderColor = GrayColor,
            errorBorderColor = OrangeColor,

            focusedLabelColor = WhiteColor,
            unfocusedLabelColor = WhiteColor,
            errorLabelColor = WhiteColor,

            cursorColor = OrangeColor
        ),
        textStyle = MaterialTheme.typography.bodyLarge
    )
}