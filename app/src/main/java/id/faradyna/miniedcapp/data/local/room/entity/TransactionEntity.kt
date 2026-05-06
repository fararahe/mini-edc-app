package id.faradyna.miniedcapp.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Representasi tabel "transactions" di database lokal.
 *
 * Digunakan untuk menyimpan data transaksi dalam bentuk Entity (data layer).
 */
@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val amount: Long,
    val type: String,
    val timestamp: Long,
    val invoiceNumber: String,
    val traceNumber: String,
    val cardPan: String,
    val status: String,
    val rrn: String? = null,
    val approvalCode: String? = null,
    val message: String? = null
)
