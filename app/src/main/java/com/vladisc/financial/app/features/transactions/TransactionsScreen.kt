package com.vladisc.financial.app.features.transactions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.vladisc.financial.app.features.user.UserViewModel
import com.vladisc.financial.app.utils.DateUtils
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(
    navController: NavController,
    userViewModel: UserViewModel,
    transactionsViewModel: TransactionsViewModel
) {
    val transactions by transactionsViewModel.transactions
    val groupedTransactions = transactions
        ?.groupBy {
            val date = DateUtils.convertDateFromISOPattern(it.timestamp.toString())
            LocalDate.parse(date)
        }
        ?.toSortedMap(compareByDescending { it })
        ?.mapKeys { it.key.format(DateTimeFormatter.ofPattern("d.M.yyyy")) }

    var loadedWeeks by remember { mutableIntStateOf(4) }
    val lazyListState = rememberLazyListState()

    LaunchedEffect(loadedWeeks) {
        val end = LocalDate.now()
        val start = end.minusDays((7L * loadedWeeks - 1))
        transactionsViewModel.getTransactions(true, start, end)
    }

    // Detect end of list reached
    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                val totalItems = lazyListState.layoutInfo.totalItemsCount
                if (lastVisibleIndex == totalItems - 1 && totalItems > 0) {
                    loadedWeeks += 1
                }
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp, 0.dp, 0.dp, 32.dp)
            .windowInsetsPadding(WindowInsets.statusBars)
            .imePadding()
    ) {
        // Header with back button
        TopAppBar(
            title = {
                Text(
                    text = "Your transactions",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .align(Alignment.CenterHorizontally)
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )

        // Transactions list
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            state = lazyListState,
        ) {
            groupedTransactions?.forEach { (date, transactionsOnDate) ->
                item {
                    Text(
                        text = date,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                    )
                }

                items(transactionsOnDate) { transaction ->
                    TransactionItem(transaction) {
                        navController.navigate("transaction/${transaction.id}")
                    }
                }
            }
        }
    }
}

@Composable
fun TransactionItem(transaction: Transaction, onClick: () -> Unit) {
    val amount = if (transaction.type == TransactionType.EXPENSE) {
        "-${DecimalFormat("0.00").format(transaction.amount)} €"
    } else {
        "${DecimalFormat("0.00").format(transaction.amount)} €"
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = transaction.name ?: "", style = MaterialTheme.typography.bodyLarge)
            Text(
                text = amount,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}