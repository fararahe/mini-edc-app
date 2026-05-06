package id.faradyna.miniedcapp.domain.model

/**
 * Representasi hasil dari proses printing.
 *
 * Dapat berupa:
 * - Success: proses cetak berhasil
 * - Error: proses cetak gagal dengan pesan error
 */
sealed class PrintResult {
    object Success : PrintResult()
    data class Error(val message: String) : PrintResult()
}