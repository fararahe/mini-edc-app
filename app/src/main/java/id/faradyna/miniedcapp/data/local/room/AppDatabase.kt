package id.faradyna.miniedcapp.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import id.faradyna.miniedcapp.data.local.room.dao.TransactionDao
import id.faradyna.miniedcapp.data.local.room.entity.TransactionEntity

/**
 * Konfigurasi database utama aplikasi menggunakan Room.
 *
 * Menyediakan akses ke DAO yang digunakan untuk operasi data lokal.
 */
@Database(entities = [TransactionEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
}
