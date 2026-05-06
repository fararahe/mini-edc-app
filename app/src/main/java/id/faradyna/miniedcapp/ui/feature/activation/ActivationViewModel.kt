package id.faradyna.miniedcapp.ui.feature.activation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.faradyna.miniedcapp.domain.model.ActivationResult
import id.faradyna.miniedcapp.domain.usecase.ActivateDeviceUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel untuk proses aktivasi EDC
 *
 * Bertanggung jawab untuk:
 * - Mengelola state UI (input terminal, merchant, dan activation code)
 * - Validasi input sederhana di sisi UI
 * - Menjalankan proses aktivasi melalui UseCase
 * - Meng-update UI state berdasarkan hasil aktivasi
 */
@HiltViewModel
class ActivationViewModel @Inject constructor(
    private val activateDeviceUseCase: ActivateDeviceUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ActivationUiState())
    val uiState: StateFlow<ActivationUiState> = _uiState.asStateFlow()

    fun onTerminalIdChange(value: String) {
        _uiState.update { it.copy(terminalId = value, error = null) }
    }

    fun onMerchantIdChange(value: String) {
        _uiState.update { it.copy(merchantId = value, error = null) }
    }

    fun onActivationCodeChange(value: String) {
        val filtered = value
            .filter { it.isDigit() }
            .take(MAX_ACTIVATION_CODE_LENGTH)

        _uiState.update {
            it.copy(
                activationCode = filtered,
                error = null
            )
        }
    }

    fun activate() {
        if (_uiState.value.isLoading) return

        val currentState = _uiState.value

        if (
            currentState.terminalId.isBlank() ||
            currentState.merchantId.isBlank() ||
            currentState.activationCode.isBlank()
        ) {
            _uiState.update { it.copy(error = "All fields are required") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val result = activateDeviceUseCase.invoke(
                terminalId = currentState.terminalId,
                merchantId = currentState.merchantId,
                activationCode = currentState.activationCode
            )

            when (result) {
                is ActivationResult.Success -> {
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                }

                is ActivationResult.Failed -> {
                    _uiState.update { it.copy(isLoading = false, error = result.message) }
                }
            }
        }
    }

    companion object {
        private const val MAX_ACTIVATION_CODE_LENGTH = 6
    }
}
