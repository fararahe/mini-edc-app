package id.faradyna.miniedcapp.ui.feature.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.faradyna.miniedcapp.domain.model.PaymentResult
import id.faradyna.miniedcapp.domain.model.Receipt
import id.faradyna.miniedcapp.domain.model.SaleTransaction
import id.faradyna.miniedcapp.domain.model.TransactionStatus
import id.faradyna.miniedcapp.domain.service.PrinterService
import id.faradyna.miniedcapp.domain.usecase.GenerateReceiptUseCase
import id.faradyna.miniedcapp.domain.usecase.GetAllTransactionsUseCase
import id.faradyna.miniedcapp.domain.usecase.GetMerchantConfigUseCase
import id.faradyna.miniedcapp.domain.usecase.RetryTransactionUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel untuk fitur History (riwayat transaksi).
 *
 * Bertanggung jawab untuk:
 * - Mengambil dan menggabungkan data transaksi dari repository
 * - Mengelola state UI (selected transaction, printing, retrying)
 * - Menyediakan preview struk (receipt) untuk transaksi yang dipilih
 * - Menangani proses cetak ulang (reprint)
 * - Menangani proses retry/sync untuk transaksi PENDING
 */
@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getMerchantConfigUseCase: GetMerchantConfigUseCase,
    private val getAllTransactionsUseCase: GetAllTransactionsUseCase,
    private val retryTransactionUseCase: RetryTransactionUseCase,
    private val generateReceiptUseCase: GenerateReceiptUseCase,
    private val printerService: PrinterService
) : ViewModel() {

    private val _selectedTransaction = MutableStateFlow<SaleTransaction?>(null)
    private val _isPrinting = MutableStateFlow(false)
    private val _isRetrying = MutableStateFlow(false)

    val uiState: StateFlow<HistoryUiState> = combine(
        getAllTransactionsUseCase(),
        getMerchantConfigUseCase(),
        _selectedTransaction,
        _isPrinting,
        _isRetrying
    ) { transactions, merchantConfig, selected, isPrinting, isRetrying ->
        var receipt: Receipt? = null
        if (selected != null) {
            receipt = generateReceiptUseCase(selected, merchantConfig.merchantName, merchantConfig.terminalId)
        }

        HistoryUiState(
            transactions = transactions,
            selectedTransaction = selected,
            receiptPreview = receipt,
            isPrinting = isPrinting,
            isRetrying = isRetrying,
            merchantId = merchantConfig.merchantId,
            terminalId = merchantConfig.terminalId,
            merchantName = merchantConfig.merchantName
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HistoryUiState())

    fun selectTransaction(transaction: SaleTransaction?) {
        _selectedTransaction.value = transaction
    }

    fun rePrint() {
        val receipt = uiState.value.receiptPreview ?: return
        viewModelScope.launch {
            _isPrinting.value = true
            printerService.print(receipt)
            _isPrinting.value = false
        }
    }

    /**
     * Melakukan retry/sync untuk transaksi dengan status PENDING.
     *
     * Flow:
     * - Hanya bisa dipanggil jika status transaksi adalah PENDING
     * - Menggunakan UseCase untuk melakukan retry ke "host"
     * - Jika berhasil, status transaksi akan diperbarui
     */
    fun retryTransaction(transaction: SaleTransaction) {
        if (transaction.status != TransactionStatus.PENDING) return
        if (_isRetrying.value) return
        
        viewModelScope.launch {
            _isRetrying.value = true
            try {
                val result = retryTransactionUseCase(transaction.invoiceNumber)
                when (result) {
                    is PaymentResult.Success -> {
                        _selectedTransaction.value = result.transaction
                    }
                    is PaymentResult.Pending -> {
                        // penanganan pending
                    }
                    is PaymentResult.Failed -> {
                        // penanganan gagal
                    }
                }
            } catch (e: Exception) {
                // handle error
            } finally {
                _isRetrying.value = false
            }
        }
    }
}
