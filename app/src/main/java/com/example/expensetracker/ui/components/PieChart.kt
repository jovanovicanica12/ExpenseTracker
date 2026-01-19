package com.example.expensetracker.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.example.expensetracker.ui.util.PieColors
import kotlin.math.min

@Composable
fun ExpensePieChart(
    data: Map<String, Double>,
    modifier: Modifier = Modifier
) {
    val total = data.values.sum()
    if (total <= 0.0) return

    val entries = remember(data) { data.entries.toList() }

    var play by remember { mutableStateOf(false) }
    val progress by animateFloatAsState(
        targetValue = if (play) 1f else 0f,
        animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing),
        label = "pie_progress"
    )
    LaunchedEffect(Unit) { play = true }

    Canvas(modifier = modifier) {
        val radius = min(size.width, size.height) / 2f
        val center = this.center

        var startAngle = -90f

        entries.forEachIndexed { index, entry ->
            val rawSweep = (entry.value / total * 360f).toFloat()
            val sweep = rawSweep * progress

            drawArc(
                color = PieColors[index % PieColors.size],
                startAngle = startAngle,
                sweepAngle = sweep,
                useCenter = true,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2)
            )

            startAngle += rawSweep
        }
    }
}
