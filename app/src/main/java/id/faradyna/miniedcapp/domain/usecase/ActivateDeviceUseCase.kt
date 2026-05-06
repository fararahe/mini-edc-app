package id.faradyna.miniedcapp.domain.usecase

import id.faradyna.miniedcapp.domain.model.ActivationResult
import id.faradyna.miniedcapp.domain.repository.AuthRepository
import javax.inject.Inject

class ActivateDeviceUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(
        terminalId: String,
        merchantId: String,
        activationCode: String
    ): ActivationResult {
        return authRepository.activate(
            terminalId = terminalId,
            merchantId = merchantId,
            activationCode = activationCode
        )
    }

}