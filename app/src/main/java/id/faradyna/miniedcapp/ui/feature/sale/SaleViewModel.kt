package id.faradyna.miniedcapp.ui.feature.sale

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.faradyna.miniedcapp.domain.model.PaymentResult
import id.faradyna.miniedcapp.domain.service.PrinterService
import id.faradyna.miniedcapp.domain.usecase.GenerateReceiptUseCase
import id.faradyna.miniedcapp.domain.usecase.GetMerchantConfigUseCase
import id.faradyna.miniedcapp.domain.usecase.ProcessSaleUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel untuk proses transaksi Sale.
 *
 * Bertanggung jawab untuk:
 * - Menerima input nominal dari user
 * - Menjalankan proses transaksi melalui UseCase
 * - Menangani hasil transaksi (success, pending, atau failed)
 * - Mengambil data merchant & terminal melalui UseCase
 * - Generate struk menggunakan GenerateReceiptUseCase
 * - Mengirim struk ke PrinterService untuk simulasi printing
 */
@HiltViewModel
class SaleViewModel @Inject constructor(
    private val processSaleUseCase: ProcessSaleUseCase,
    private val getMerchantConfigUseCase: GetMerchantConfigUseCase,
    private val generateReceiptUseCase: GenerateReceiptUseCase,
    private val printerService: PrinterService
) : ViewModel() {

    private val _uiState = MutableStateFlow(SaleUiState())
    val uiState: StateFlow<SaleUiState> = _uiState.asStateFlow()

    fun onAmountChange(value: String) {
        if (value.all { it.isDigit() }) {
            _uiState.update { it.copy(amount = value, error = null) }
        }
    }

    fun processSale() {
        if (_uiState.value.isLoading) return

        val amountStr = _uiState.value.amount
        
        if (amountStr.isBlank() || amountStr.toLong() <= 0) {
            _uiState.update { it.copy(error = "Please enter a valid amount") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, transactionResult = null, receipt = null) }
            try {
                val result = processSaleUseCase(amountStr.toLong())
                
                when (result) {
                    is PaymentResult.Success -> {
                        val config = getMerchantConfigUseCase().first()
                        val generatedReceipt = generateReceiptUseCase(result.transaction, config.merchantName, config.terminalId)
                        
                        _uiState.update { it.copy(
                            isLoading = false, 
                            transactionResult = result.transaction,
                            receipt = generatedReceipt
                        ) }
                    }
                    is PaymentResult.Pending -> {
                        _uiState.update { it.copy(
                            isLoading = false,
                            transactionResult = result.transaction,
                            error = "Transaction Pending: Host timeout"
                        ) }
                    }
                    is PaymentResult.Failed -> {
                        _uiState.update { it.copy(
                            isLoading = false,
                            transactionResult = result.transaction,
                            error = result.message
                        ) }
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message ?: "Transaction failed") }
            }
        }
    }

    fun printReceipt() {
        val receipt = _uiState.value.receipt ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isPrinting = true) }
            printerService.print(receipt)
            _uiState.update { it.copy(isPrinting = false) }
        }
    }

    fun resetState() {
        _uiState.update { SaleUiState() }
    }
}
