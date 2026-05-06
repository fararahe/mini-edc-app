package id.faradyna.miniedcapp.ui.feature.history

import id.faradyna.miniedcapp.domain.model.Receipt
import id.faradyna.miniedcapp.domain.model.SaleTransaction

data class HistoryUiState(
    val transactions: List<SaleTransaction> = emptyList(),
    val selectedTransaction: SaleTransaction? = null,
    val receiptPreview: Receipt? = null,
    val isPrinting: Boolean = false,
    val isRetrying: Boolean = false,
    val merchantId: String = "",
    val terminalId: String = "",
    val merchantName: String = ""
)
