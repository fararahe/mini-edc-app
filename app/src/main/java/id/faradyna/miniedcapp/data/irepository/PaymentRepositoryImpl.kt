package id.faradyna.miniedcapp.data.irepository

import id.faradyna.miniedcapp.data.local.datastore.AuthDataStore
import id.faradyna.miniedcapp.domain.model.PaymentResult
import id.faradyna.miniedcapp.domain.model.SaleTransaction
import id.faradyna.miniedcapp.domain.model.TransactionStatus
import id.faradyna.miniedcapp.domain.repository.PaymentRepository
import id.faradyna.miniedcapp.domain.repository.TransactionRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementasi PaymentRepository / flow utama transaksi pembayaran
 *
 * Implementasi ini mensimulasikan pembayaran melalui terminal EDC:
 * - Generate trace number untuk memastikan setiap transaksi unik
 * - Simulasi delay seperti network/API call
 * - Penerapan business rules berdasarkan nominal (untuk kebutuhan testing)
 * - Penyimpanan transaksi ke local storage untuk kebutuhan history
 */
@Singleton
class PaymentRepositoryImpl @Inject constructor(
    private val authDataStore: AuthDataStore,
    private val transactionRepository: TransactionRepository
) : PaymentRepository {

    /**
     * Flow utama transaksi sale:
     * - Validasi status aktivasi (token)
     * - Generate trace number
     * - Simulasi delay proses ke host seperti network/API call
     * - Menentukan hasil transaksi berdasarkan rule (last digit - selain 1 & 2: Success, 1: Pending, 2: Failed)
     * - Menyimpan transaksi ke local repository
     * - Mengembalikan hasil dalam bentuk PaymentResult
     */
    override suspend fun processSale(amount: Long): PaymentResult {
        val token = authDataStore.authToken.first()
        if (token.isNullOrEmpty()) {
            return PaymentResult.Failed("Unauthorized: Terminal is not activated")
        }

        val traceNumber = authDataStore.getAndIncrementTraceNumber()

        delay(2000)

        val lastDigit = amount % 10
        val status = when (lastDigit) {
            1L -> TransactionStatus.PENDING
            2L -> TransactionStatus.FAILED
            else -> TransactionStatus.SUCCESS
        }

        val message = when (status) {
            TransactionStatus.SUCCESS -> "Approved"
            TransactionStatus.PENDING -> "Transaction Pending"
            TransactionStatus.FAILED -> "Transaction Failed"
        }

        val saleTransaction = SaleTransaction(
            amount = amount,
            type = "SALE",
            timestamp = System.currentTimeMillis(),
            invoiceNumber = traceNumber,
            traceNumber = traceNumber,
            cardPan = "**** **** **** 1234",
            status = status,
            rrn = "RRN" + System.currentTimeMillis().toString().takeLast(8),
            approvalCode = traceNumber.takeLast(6),
            message = message
        )

        transactionRepository.insertTransaction(saleTransaction)

        return when (status) {
            TransactionStatus.SUCCESS -> PaymentResult.Success(saleTransaction)
            TransactionStatus.PENDING -> PaymentResult.Pending(saleTransaction)
            TransactionStatus.FAILED -> PaymentResult.Failed("Transaction Failed", saleTransaction)
        }
    }

    /**
     * Mekanisme retry / sync untuk transaksi pending.
     *
     * Syarat:
     * - Hanya transaksi dengan status PENDING yang bisa di-retry
     * - Dilakukan simulasi delay seperti proses inquiry ke host
     * - Hasil retry bisa berhasil atau gagal (simulasi)
     * - Jika berhasil, status transaksi akan di-update menjadi SUCCESS
     */
    override suspend fun retryPendingTransaction(invoiceNumber: String): PaymentResult {
        val token = authDataStore.authToken.first()
        if (token.isNullOrEmpty()) {
            return PaymentResult.Failed("Unauthorized: Please activate the terminal")
        }

        val existing = transactionRepository.getTransactionByInvoice(invoiceNumber)
            ?: return PaymentResult.Failed("Transaction not found")

        if (existing.status != TransactionStatus.PENDING) {
            return PaymentResult.Failed("Only PENDING transactions can be retried")
        }

        delay(1500)

        val updatedTransaction = existing.copy(
            status = TransactionStatus.SUCCESS,
            message = "Approved (Synced)",
            timestamp = System.currentTimeMillis()
        )

        transactionRepository.updateTransaction(updatedTransaction)
        
        return PaymentResult.Success(updatedTransaction)
    }
}
