package id.faradyna.miniedcapp.ui.feature.sale

import id.faradyna.miniedcapp.domain.model.Receipt
import id.faradyna.miniedcapp.domain.model.SaleTransaction

data class SaleUiState(
    val amount: String = "",
    val isLoading: Boolean = false,
    val isPrinting: Boolean = false,
    val error: String? = null,
    val transactionResult: SaleTransaction? = null,
    val receipt: Receipt? = null
)