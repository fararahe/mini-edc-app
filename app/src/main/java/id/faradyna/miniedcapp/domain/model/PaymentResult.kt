package id.faradyna.miniedcapp.domain.model

/**
 * Representasi hasil proses pembayaran.
 *
 * Bisa berupa:
 * - Success: transaksi berhasil diproses
 * - Pending: transaksi berada di Pending State (e.g. Timeout) yang memerlukan Sync/Retry
 * - Failed: transaksi gagal diproses karna error / kesalahan sistem
 */
sealed class PaymentResult {
    data class Success(val transaction: SaleTransaction) : PaymentResult()

    data class Pending(val transaction: SaleTransaction) : PaymentResult()

    data class Failed(val message: String, val transaction: SaleTransaction? = null) : PaymentResult()
}
