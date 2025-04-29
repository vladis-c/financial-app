package com.vladisc.financial.app.features.transactions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.vladisc.financial.app.features.user.UserViewModel
import com.vladisc.financial.app.utils.DateUtils.convertDateFromPatternToISO
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionScreen(
    navController: NavController,
    userViewModel: UserViewModel,
    transactionsViewModel: TransactionsViewModel,
    notificationsViewModel: PushNotificationsViewModel,
    transactionId: String
) {
    val transactions by transactionsViewModel.transactions
    val transaction = transactions?.find { it.id == transactionId }

    val notification by notificationsViewModel.pushNotification

    val scrollState = rememberScrollState()

    val date = LocalDate.parse(convertDateFromPatternToISO(transaction?.timestamp.toString(), "yyyy-MM-dd'T'HH:mm")).format(DateTimeFormatter.ofPattern("d.M.yyyy"))
    val amount = if(transaction?.type == TransactionType.EXPENSE) {
        "-${DecimalFormat("0.00").format(transaction.amount)} €"
    } else {
        "${DecimalFormat("0.00").format(transaction?.amount)} €"
    }

    LaunchedEffect(transactionId) {
        notificationsViewModel.getNotification(true, transactionId)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 0.dp, 0.dp, 32.dp)
            .windowInsetsPadding(WindowInsets.statusBars)
            .verticalScroll(scrollState)
            .imePadding()
            .wrapContentSize(Alignment.Center),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "Transaction",
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
            })
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text(
                text = transaction?.name ?: "",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(16.dp, 16.dp, 16.dp, 0.dp)
            )
            Text(
                text = transaction?.type.toString().lowercase().replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            )
            Text(
                text = date,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            )
            Text(
                text = amount,
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier
                    .padding(16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Associated push notification",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            )
            Text(
                text = "${notification?.title}. ${notification?.body}",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

        }
    }
}