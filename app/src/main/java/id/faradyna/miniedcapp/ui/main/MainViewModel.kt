package id.faradyna.miniedcapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.faradyna.miniedcapp.domain.usecase.GetDeviceInfoUseCase
import id.faradyna.miniedcapp.domain.usecase.GetMerchantConfigUseCase
import id.faradyna.miniedcapp.domain.usecase.IsActivatedUseCase
import id.faradyna.miniedcapp.domain.usecase.LogoutUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val isActivatedUseCase: IsActivatedUseCase,
    private val getMerchantConfigUseCase: GetMerchantConfigUseCase,
    private val getDeviceInfoUseCase: GetDeviceInfoUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    val uiState: StateFlow<MainUiState> = combine(
        isActivatedUseCase(),
        getMerchantConfigUseCase()
    ) { isActivated, merchantConfig ->
        val deviceInfo = getDeviceInfoUseCase()
        MainUiState(
            isActivated = isActivated,
            merchantName = merchantConfig.merchantName,
            terminalId = merchantConfig.terminalId,
            merchantId = merchantConfig.merchantId,
            deviceSn = deviceInfo.serialNumber,
            deviceModel = deviceInfo.model
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MainUiState())

    fun logout() {
        viewModelScope.launch {
            logoutUseCase()
        }
    }
}
