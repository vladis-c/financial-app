package com.vladisc.financial.app.features.transactions

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TransactionsViewModel : ViewModel() {
    private val _transactions = mutableStateOf<List<Transaction>?>(null)
    val transactions: State<List<Transaction>?> = _transactions

    fun getTransactions(forceRefresh: Boolean = false, start: LocalDate?, end: LocalDate?) {
        viewModelScope.launch {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            try {
                _transactions.value = TransactionsApi.get(
                    forceRefresh,
                    start?.format(formatter),
                    end?.format(formatter)
                )
            } catch (e: Exception) {
                println("Failed to fetch transactions: ${e.message}")
            }
        }
    }
}