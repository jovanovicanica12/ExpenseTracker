package com.example.expensetracker.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.expensetracker.R
import com.example.expensetracker.data.ExpenseCategory
import com.example.expensetracker.data.local.ExpenseEntity
import com.example.expensetracker.ui.theme.GrayColor
import com.example.expensetracker.ui.theme.OrangeColor
import com.example.expensetracker.ui.theme.WhiteColor
import com.example.expensetracker.ui.util.formatMoney

@Composable
fun SummaryCards(
    total: Double,
    count: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SummarySmallCard(
            title = stringResource(R.string.total),
            value = formatMoney(total)
        )
        SummarySmallCard(
            title = stringResource(R.string.count),
            value = count.toString()
        )
    }
}

@Composable
private fun RowScope.SummarySmallCard(
    title: String,
    value: String
) {
    Card(
        modifier = Modifier
            .weight(1f)
            .height(88.dp),
        colors = CardDefaults.cardColors(containerColor = GrayColor)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(title, color = WhiteColor)
            Text(
                value,
                color = WhiteColor,
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}

@Composable
fun CategoryFilterRow(
    selected: ExpenseCategory?,
    onSelect: (ExpenseCategory?) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CategoryChip(text = stringResource(R.string.all), selected = selected == null) {
            onSelect(null)
        }

        ExpenseCategory.values().forEach { category ->
            CategoryChip(text = category.name, selected = selected == category) {
                onSelect(category)
            }
        }
    }
}

@Composable
private fun CategoryChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(text, color = WhiteColor) },
        shape = RoundedCornerShape(12.dp),
        colors = FilterChipDefaults.filterChipColors(
            containerColor = GrayColor,
            selectedContainerColor = OrangeColor,
            labelColor = WhiteColor,
            selectedLabelColor = WhiteColor
        )
    )
}

@Composable
fun ExpenseRow(
    expense: ExpenseEntity,
    onClick: () -> Unit,
    onDelete: (ExpenseEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = GrayColor)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = expense.description.ifBlank { stringResource(R.string.no_description) },
                    color = WhiteColor,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.height(4.dp))
                Text(expense.category.name, color = WhiteColor)
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = formatMoney(expense.amount),
                    color = WhiteColor,
                    style = MaterialTheme.typography.titleMedium
                )
                IconButton(onClick = { onDelete(expense) }) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = stringResource(R.string.delete),
                        tint = OrangeColor
                    )
                }
            }
        }
    }
}