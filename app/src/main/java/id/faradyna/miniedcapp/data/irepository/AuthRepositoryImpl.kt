package id.faradyna.miniedcapp.data.irepository

import id.faradyna.miniedcapp.data.local.datastore.AuthDataStore
import id.faradyna.miniedcapp.domain.model.ActivationResult
import id.faradyna.miniedcapp.domain.repository.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authDataStore: AuthDataStore
): AuthRepository {

    override fun isActivated(): Flow<Boolean> = authDataStore.isActivated

    override fun getTerminalId(): Flow<String?> = authDataStore.terminalId

    override fun getMerchantId(): Flow<String?> = authDataStore.merchantId

    override fun getMerchantName(): Flow<String?> = authDataStore.merchantName

    /**
     * Melakukan proses aktivasi EDC (simulasi API).
     *
     * Flow:
     * - Delay untuk simulasi network call
     * - Validasi kode aktivasi sederhana
     * - Menyimpan data hasil aktivasi ke DataStore jika sukses
     */
    override suspend fun activate(
        terminalId: String,
        merchantId: String,
        activationCode: String
    ): ActivationResult {
        delay(1500)

        return if (activationCode == MOCK_ACTIVATION_CODE) {
            val merchantName = MOCK_MERCHANT_NAME
            val token = MOCK_TOKEN

            authDataStore.saveActivationData(
                terminalId = terminalId,
                merchantId = merchantId,
                merchantName = merchantName,
                token = token
            )

            ActivationResult.Success(terminalId, merchantName, token)
        } else {
            ActivationResult.Failed(FAILED_MESSAGE)
        }
    }

    override suspend fun logout() {
        authDataStore.clear()
    }

    companion object {
        private const val MOCK_ACTIVATION_CODE = "123456"
        private const val MOCK_MERCHANT_NAME = "Toko Maju Jaya"
        private const val MOCK_TOKEN = "mock-token-123"
        private const val FAILED_MESSAGE = "Invalid activation code"
    }

}