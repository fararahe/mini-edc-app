package id.faradyna.miniedcapp.domain.usecase

import id.faradyna.miniedcapp.domain.model.PaymentResult
import id.faradyna.miniedcapp.domain.repository.PaymentRepository
import javax.inject.Inject

class RetryTransactionUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository
) {
    suspend operator fun invoke(invoiceNumber: String): PaymentResult {
        return paymentRepository.retryPendingTransaction(invoiceNumber)
    }
}
