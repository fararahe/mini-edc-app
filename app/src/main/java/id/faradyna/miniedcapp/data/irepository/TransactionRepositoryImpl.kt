package id.faradyna.miniedcapp.data.irepository

import id.faradyna.miniedcapp.data.local.room.dao.TransactionDao
import id.faradyna.miniedcapp.data.mapper.toDomain
import id.faradyna.miniedcapp.data.mapper.toEntity
import id.faradyna.miniedcapp.domain.model.SaleTransaction
import id.faradyna.miniedcapp.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementasi TransactionRepository menggunakan Room (DAO).
 *
 * Berfungsi sebagai penghubung antara layer Domain dan database lokal.
 * Bertanggung jawab untuk:
 * - Mengambil data transaksi dari database
 * - Menyimpan dan memperbarui data transaksi
 * - Melakukan mapping antara Entity (data layer) dan Domain model
 */
@Singleton
class TransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao
) : TransactionRepository {
    override fun getAllTransactions(): Flow<List<SaleTransaction>> =
        transactionDao.getAllTransactions().map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun insertTransaction(saleTransaction: SaleTransaction) {
        transactionDao.insertTransaction(saleTransaction.toEntity())
    }

    override suspend fun updateTransaction(saleTransaction: SaleTransaction) {
        transactionDao.insertTransaction(saleTransaction.toEntity())
    }

    override suspend fun getTransactionByInvoice(invoiceNumber: String): SaleTransaction? {
        return transactionDao.getTransactionByInvoice(invoiceNumber)?.toDomain()
    }

    override suspend fun clearTransactions() {
        transactionDao.deleteAllTransactions()
    }
}
