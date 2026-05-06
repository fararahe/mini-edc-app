package id.faradyna.miniedcapp.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.faradyna.miniedcapp.data.local.room.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO (Data Access Object) untuk operasi database transaksi.
 */
@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity)

    @Query("SELECT * FROM transactions WHERE invoiceNumber = :invoiceNumber")
    suspend fun getTransactionByInvoice(invoiceNumber: String): TransactionEntity?

    @Query("DELETE FROM transactions")
    suspend fun deleteAllTransactions()
}
