package com.example.expensetracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.expensetracker.R
import com.example.expensetracker.data.ExpenseCategory
import com.example.expensetracker.ui.ExpenseViewModel
import com.example.expensetracker.ui.components.CenterMessage
import com.example.expensetracker.ui.components.ExpenseBarChart
import com.example.expensetracker.ui.components.ExpensePieChart
import com.example.expensetracker.ui.components.LegendRowWithPercent
import com.example.expensetracker.ui.components.RangeChipUi
import com.example.expensetracker.ui.components.RangeChipsRow
import com.example.expensetracker.ui.theme.BlackColor
import com.example.expensetracker.ui.theme.WhiteColor
import com.example.expensetracker.ui.util.PieColors
import com.example.expensetracker.ui.util.formatMoney
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(
    vm: ExpenseViewModel,
    onBack: () -> Unit
) {
    val allExpenses by vm.allExpenses.collectAsState()

    var range by remember { mutableStateOf(DateRangeOption.All) }

    val nowMillis = System.currentTimeMillis()
    val zone = ZoneId.systemDefault()

    val startMillis: Long? =
        when (range) {
            DateRangeOption.All -> null
            DateRangeOption.Last7Days -> nowMillis - 7L * 24 * 60 * 60 * 1000
            DateRangeOption.Last30Days -> nowMillis - 30L * 24 * 60 * 60 * 1000
            DateRangeOption.ThisMonth -> {
                val today = LocalDate.now(zone)
                today.withDayOfMonth(1)
                    .atStartOfDay(zone)
                    .toInstant()
                    .toEpochMilli()
            }
        }

    val endMillis: Long? =
        when (range) {
            DateRangeOption.All -> null
            DateRangeOption.Last7Days,
            DateRangeOption.Last30Days,
            DateRangeOption.ThisMonth -> nowMillis
        }

    val filteredExpenses =
        if (startMillis == null || endMillis == null) {
            allExpenses
        } else {
            allExpenses.filter { it.createdAtMillis in startMillis..endMillis }
        }

    val totalsByCategory = remember(filteredExpenses) {
        ExpenseCategory.values().associate { category ->
            category.name to filteredExpenses
                .filter { it.category == category }
                .sumOf { it.amount }
        }
    }

    val pieData = remember(totalsByCategory) {
        totalsByCategory
            .filterValues { it > 0.0 }
            .toSortedMap()
    }

    val total = totalsByCategory.values.sum()

    Scaffold(
        containerColor = BlackColor,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.stats), color = WhiteColor) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                            tint = WhiteColor
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BlackColor)
            )
        }
    ) { padding ->

        if (filteredExpenses.isEmpty() || pieData.isEmpty()) {
            CenterMessage(
                text = stringResource(R.string.no_statistics_period),
                modifier = Modifier.padding(padding)
            )
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            RangeChipsRow(
                chips = listOf(
                    RangeChipUi(
                        text = stringResource(R.string.range_all),
                        selected = range == DateRangeOption.All,
                        onClick = { range = DateRangeOption.All }
                    ),
                    RangeChipUi(
                        text = stringResource(R.string.range_7d),
                        selected = range == DateRangeOption.Last7Days,
                        onClick = { range = DateRangeOption.Last7Days }
                    ),
                    RangeChipUi(
                        text = stringResource(R.string.range_30d),
                        selected = range == DateRangeOption.Last30Days,
                        onClick = { range = DateRangeOption.Last30Days }
                    ),
                    RangeChipUi(
                        text = stringResource(R.string.range_month),
                        selected = range == DateRangeOption.ThisMonth,
                        onClick = { range = DateRangeOption.ThisMonth }
                    )
                )
            )

            Spacer(Modifier.height(14.dp))

            Text(
                text = stringResource(R.string.total_amount, formatMoney(total)),
                color = WhiteColor,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(Modifier.height(18.dp))

            ExpensePieChart(
                data = pieData,
                modifier = Modifier.size(220.dp)
            )

            Spacer(Modifier.height(18.dp))

            ExpenseBarChart(
                data = pieData,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(18.dp))

            pieData.entries.forEachIndexed { index, entry ->
                LegendRowWithPercent(
                    color = PieColors[index % PieColors.size],
                    label = entry.key,
                    value = entry.value,
                    total = total
                )
            }
        }
    }
}

private enum class DateRangeOption {
    All, Last7Days, Last30Days, ThisMonth
}