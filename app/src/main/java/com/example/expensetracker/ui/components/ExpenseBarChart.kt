package com.example.expensetracker.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.expensetracker.ui.theme.WhiteColor
import com.example.expensetracker.ui.util.PieColors

@Composable
fun ExpenseBarChart(
    data: Map<String, Double>,
    modifier: Modifier = Modifier
) {
    val entries = remember(data) { data.entries.toList() }
    val maxValue = entries.maxOfOrNull { it.value } ?: 0.0
    if (entries.isEmpty() || maxValue <= 0.0) return

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        ) {
            val barCount = entries.size
            if (barCount == 0) return@Canvas

            val spacing = size.width * 0.06f
            val totalSpacing = spacing * (barCount + 1)
            val barWidth = (size.width - totalSpacing) / barCount
            val chartHeight = size.height

            entries.forEachIndexed { index, entry ->
                val ratio = (entry.value / maxValue).toFloat().coerceIn(0f, 1f)
                val barHeight = chartHeight * ratio

                val left = spacing + index * (barWidth + spacing)
                val top = chartHeight - barHeight

                drawRoundRect(
                    color = PieColors[index % PieColors.size],
                    topLeft = Offset(left, top),
                    size = Size(barWidth, barHeight),
                    cornerRadius = CornerRadius(16f, 16f)
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            entries.forEach { entry ->
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Text(
                        text = entry.key, //
                        color = WhiteColor.copy(alpha = 0.85f),
                        style = MaterialTheme.typography.labelSmall,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        softWrap = true
                    )
                }
            }
        }
    }
}