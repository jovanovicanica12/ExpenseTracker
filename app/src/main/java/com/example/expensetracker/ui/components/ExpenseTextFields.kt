package com.example.expensetracker.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.example.expensetracker.ui.theme.GrayColor
import com.example.expensetracker.ui.theme.OrangeColor
import com.example.expensetracker.ui.theme.WhiteColor

@Composable
fun ExpenseTextField(
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
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        label = { Text(text = label) },
        singleLine = singleLine,
        isError = isError,
        readOnly = readOnly,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        trailingIcon = trailing,
        textStyle = MaterialTheme.typography.bodyLarge,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = GrayColor,
            unfocusedContainerColor = GrayColor,
            disabledContainerColor = GrayColor,
            errorContainerColor = GrayColor,

            focusedTextColor = WhiteColor,
            unfocusedTextColor = WhiteColor,
            errorTextColor = WhiteColor,

            focusedIndicatorColor = OrangeColor,
            unfocusedIndicatorColor = GrayColor,
            errorIndicatorColor = OrangeColor,

            focusedLabelColor = WhiteColor,
            unfocusedLabelColor = WhiteColor,
            errorLabelColor = WhiteColor,

            cursorColor = OrangeColor
        )
    )
}