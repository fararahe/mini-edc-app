package id.faradyna.miniedcapp.domain.model

/**
 * Model domain untuk merepresentasikan transaksi (Sale).
 *
 * Berisi seluruh informasi penting terkait transaksi,
 * seperti nominal, status, identitas transaksi, dan data kartu.
 *
 * Digunakan sebagai source of truth di domain layer
 * dan menjadi dasar untuk proses bisnis.
 */
data class SaleTransaction(
    val id: Long = 0,
    val amount: Long,
    val type: String,
    val timestamp: Long,
    val invoiceNumber: String,
    val traceNumber: String,
    val cardPan: String,
    val status: TransactionStatus,
    val rrn: String? = null,
    val approvalCode: String? = null,
    val message: String? = null
)
