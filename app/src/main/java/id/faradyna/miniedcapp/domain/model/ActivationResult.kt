package id.faradyna.miniedcapp.domain.model

/**
 * Representasi hasil proses aktivasi device.
 *
 * Bisa berupa:
 * - Success: aktivasi berhasil dan mengembalikan data terminal + merchant + token
 * - Failed: aktivasi gagal dengan pesan error
 */
sealed class ActivationResult {
    data class Success(val terminalId: String, val merchantName: String, val token: String) : ActivationResult()
    data class Failed(val message: String) : ActivationResult()
}
