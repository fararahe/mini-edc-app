package id.faradyna.miniedcapp.domain.model

/**
 * Enum untuk merepresentasikan status transaksi.
 *
 * Digunakan di domain layer untuk menentukan hasil akhir transaksi:
 * - SUCCESS: transaksi berhasil
 * - FAILED: transaksi gagal
 * - PENDING: transaksi belum final (perlu retry/sync)
 *
 * Menyediakan helper `from()` untuk konversi String ke enum secara aman.
 */
enum class TransactionStatus {
    SUCCESS,
    FAILED,
    PENDING;

    companion object {
        fun from(value: String): TransactionStatus {
            return entries.find { it.name == value } ?: FAILED
        }
    }
}
