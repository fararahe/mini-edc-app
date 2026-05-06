package id.faradyna.miniedcapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.faradyna.miniedcapp.domain.device.DeviceInfoProvider
import id.faradyna.miniedcapp.domain.repository.AuthRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val deviceInfoProvider: DeviceInfoProvider
) : ViewModel() {

    val uiState: StateFlow<MainUiState> = combine(
        authRepository.isActivated(),
        authRepository.getMerchantName(),
        authRepository.getTerminalId(),
        authRepository.getMerchantId()
    ) { isActivated, merchantName, terminalId, merchantId ->
        MainUiState(
            isActivated = isActivated,
            merchantName = merchantName,
            terminalId = terminalId,
            merchantId = merchantId,
            deviceSn = deviceInfoProvider.getSerialNumber(),
            deviceModel = deviceInfoProvider.getDeviceModel()
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MainUiState())

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}
