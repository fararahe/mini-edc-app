package id.faradyna.miniedcapp.domain.repository

import id.faradyna.miniedcapp.domain.model.PaymentResult

/**
 * Interface untuk menangani proses pembayaran.
 */
interface PaymentRepository {
    suspend fun processSale(amount: Long): PaymentResult
    suspend fun retryPendingTransaction(invoiceNumber: String): PaymentResult
}
