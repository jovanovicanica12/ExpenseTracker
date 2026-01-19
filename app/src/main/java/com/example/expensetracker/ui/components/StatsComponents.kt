package com.example.expensetracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.expensetracker.ui.theme.GrayColor
import com.example.expensetracker.ui.theme.OrangeColor
import com.example.expensetracker.ui.theme.WhiteColor
import com.example.expensetracker.ui.util.formatMoney

@Composable
fun RangeChipsRow(
    chips: List<RangeChipUi>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        chips.forEach { chip ->
            RangeChip(
                text = chip.text,
                selected = chip.selected,
                onClick = chip.onClick
            )
        }
    }
}

data class RangeChipUi(
    val text: String,
    val selected: Boolean,
    val onClick: () -> Unit
)

@Composable
private fun RangeChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(text, color = WhiteColor) },
        colors = FilterChipDefaults.filterChipColors(
            containerColor = GrayColor,
            selectedContainerColor = OrangeColor,
            labelColor = WhiteColor,
            selectedLabelColor = WhiteColor
        )
    )
}

@Composable
fun LegendRowWithPercent(
    color: androidx.compose.ui.graphics.Color,
    label: String,
    value: Double,
    total: Double,
    modifier: Modifier = Modifier
) {
    val percent = if (total > 0.0) ((value / total) * 100).toInt() else 0

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color, shape = CircleShape)
        )

        Spacer(Modifier.width(8.dp))

        Text(
            text = "$label • $percent%",
            modifier = Modifier.weight(1f),
            color = WhiteColor,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 2,
            softWrap = true
        )
        Spacer(Modifier.width(8.dp))

        Text(
            text = formatMoney(value),
            color = WhiteColor,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1
        )
    }
}

@Composable
fun CenterMessage(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = WhiteColor,
            style = MaterialTheme.typography.headlineSmall
        )
    }
}