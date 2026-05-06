package id.faradyna.miniedcapp.domain.usecase

import id.faradyna.miniedcapp.domain.model.SaleTransaction
import id.faradyna.miniedcapp.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllTransactionsUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(): Flow<List<SaleTransaction>> {
        return transactionRepository.getAllTransactions()
    }
}
