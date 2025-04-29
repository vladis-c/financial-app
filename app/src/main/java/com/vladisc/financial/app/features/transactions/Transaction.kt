package com.vladisc.financial.app.features.transactions

import kotlinx.serialization.Serializable

@Serializable
data class Transaction(
    val id: String? = null,
    val timestamp: String? = null,
    val amount: Float? = null,
    val name: String? = null,
    val type: TransactionType? = null,
    val editedBy: EditedBy? = null,
    val dueDate: String? = null,
    val payDate: String? = null,
    val invoiceStatus: InvoiceStatus? = null
)

enum class TransactionType { INCOME, EXPENSE, INVOICE, REFUND, TRANSFER, DIVIDEND }

enum class InvoiceStatus {
    /** Automatically confirmed. Before due_date: CONFIRMED, after due_date: PAID. Add pay_date when PAID */
    CONFIRMED,
    /** Automatically not confirmed. Before due_date: UNCONFIRMED, after due_date: UNPAID */
    UNCONFIRMED,
    /** When invoice has been canceled. Set manually. Keep due_date, remove pay_date */
    CANCELED,
    /** Set to status PAID either manually or if it was CONFIRMED and over due_date. Add pay_date */
    PAID,
    /** Set to status UNPAID either manually or if it was UNCONFIRMED and over due_date */
    UNPAID
}
enum class EditedBy { AUTO, USER }

