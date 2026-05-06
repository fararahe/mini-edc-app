package id.faradyna.miniedcapp.domain.repository

import id.faradyna.miniedcapp.domain.model.ActivationResult
import kotlinx.coroutines.flow.Flow

/**
 * Interface untuk autentikasi dan aktivasi EDC.
 *
 * Menyediakan akses data terkait status aktivasi, informasi merchant,
 * serta proses aktivasi dan logout.
 */
interface AuthRepository {
    fun isActivated(): Flow<Boolean>
    fun getTerminalId(): Flow<String?>
    fun getMerchantId(): Flow<String?>
    fun getMerchantName(): Flow<String?>
    suspend fun activate(terminalId: String, merchantId: String, activationCode: String): ActivationResult
    suspend fun logout()
}