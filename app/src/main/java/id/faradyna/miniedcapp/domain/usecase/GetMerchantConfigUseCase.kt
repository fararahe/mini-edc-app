package id.faradyna.miniedcapp.domain.usecase

import id.faradyna.miniedcapp.domain.model.MerchantConfig
import id.faradyna.miniedcapp.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetMerchantConfigUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Flow<MerchantConfig> {
        return combine(
            authRepository.getMerchantId(),
            authRepository.getTerminalId(),
            authRepository.getMerchantName()
        ) { mid, tid, name ->
            MerchantConfig(
                merchantId = mid ?: "-",
                terminalId = tid ?: "-",
                merchantName = name ?: "-"
            )
        }
    }
}
