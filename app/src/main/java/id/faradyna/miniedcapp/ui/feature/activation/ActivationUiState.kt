package id.faradyna.miniedcapp.ui.feature.activation

data class ActivationUiState(
    val terminalId: String = "",
    val merchantId: String = "",
    val activationCode: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)
