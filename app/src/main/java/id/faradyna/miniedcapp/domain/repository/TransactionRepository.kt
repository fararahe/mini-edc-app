package id.faradyna.miniedcapp.domain.repository

import id.faradyna.miniedcapp.domain.model.SaleTransaction
import kotlinx.coroutines.flow.Flow

/**
 * Interface untuk mengelola data transaksi di penyimpanan lokal.
 */
interface TransactionRepository {
    fun getAllTransactions(): Flow<List<SaleTransaction>>
    suspend fun insertTransaction(saleTransaction: SaleTransaction)
    suspend fun updateTransaction(saleTransaction: SaleTransaction)
    suspend fun getTransactionByInvoice(invoiceNumber: String): SaleTransaction?
    suspend fun clearTransactions()
}
