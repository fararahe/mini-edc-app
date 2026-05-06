package id.faradyna.miniedcapp.data.irepository

import id.faradyna.miniedcapp.data.local.datastore.AuthDataStore
import id.faradyna.miniedcapp.domain.model.ActivationResult
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AuthRepositoryImplTest {

    private lateinit var authDataStore: AuthDataStore
    private lateinit var authRepository: AuthRepositoryImpl

    @Before
    fun setUp() {
        authDataStore = mockk(relaxed = true)
        authRepository = AuthRepositoryImpl(authDataStore)
    }

    @Test
    fun `aktivasi sukses dan menyimpan data`() = runTest {
        // Given
        val terminalId = "TID123"
        val merchantId = "MID456"
        val activationCode = "123456"

        // Result
        val merchantName = "Toko Maju Jaya"
        val token = "mock-token-123"

        // When
        val result = authRepository.activate(terminalId, merchantId, activationCode)

        // Then
        assertTrue(result is ActivationResult.Success)
        val successResult = result as ActivationResult.Success
        assertEquals(terminalId, successResult.terminalId)
        assertEquals(merchantName, successResult.merchantName)

        coVerify {
            authDataStore.saveActivationData(
                terminalId = terminalId,
                merchantId = merchantId,
                merchantName = merchantName,
                token = token
            )
        }
    }

    @Test
    fun `aktivasi gagal dan mengembalikan Failed`() = runTest {
        // Given
        val terminalId = "TID123"
        val merchantId = "MID456"
        val activationCode = "000000"

        // Result
        val errorMessage = "Invalid activation code"

        // When
        val result = authRepository.activate(terminalId, merchantId, activationCode)

        // Then
        assertTrue(result is ActivationResult.Failed)
        assertEquals(errorMessage, (result as ActivationResult.Failed).message)
        
        coVerify(exactly = 0) {
            authDataStore.saveActivationData(any(), any(), any(), any())
        }
    }

    @Test
    fun `logout menghapus DataStore`() = runTest {
        // When
        authRepository.logout()

        // Then
        coVerify { authDataStore.clear() }
    }
}
