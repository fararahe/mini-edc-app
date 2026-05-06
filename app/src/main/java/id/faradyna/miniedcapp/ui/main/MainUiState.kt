package id.faradyna.miniedcapp.ui.main

data class MainUiState(
    val isActivated: Boolean? = null,
    val merchantName: String? = null,
    val terminalId: String? = null,
    val merchantId: String? = null,
    val deviceSn: String = "",
    val deviceModel: String = ""
)