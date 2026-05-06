package id.faradyna.miniedcapp.domain.usecase

import id.faradyna.miniedcapp.domain.model.PaymentResult
import id.faradyna.miniedcapp.domain.repository.PaymentRepository
import javax.inject.Inject

class ProcessSaleUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository
) {
    suspend operator fun invoke(amount: Long): PaymentResult {
        return paymentRepository.processSale(amount)
    }
}
