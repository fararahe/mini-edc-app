package id.faradyna.miniedcapp.ui.feature.activation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import id.faradyna.miniedcapp.R
import id.faradyna.miniedcapp.ui.theme.MiniEDCMerchantAppTheme

@Composable
fun ActivationScreen(
    onActivationSuccess: () -> Unit,
    viewModel: ActivationViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    ActivationContent(
        uiState = uiState,
        onTerminalIdChange = viewModel::onTerminalIdChange,
        onMerchantIdChange = viewModel::onMerchantIdChange,
        onActivationCodeChange = viewModel::onActivationCodeChange,
        onActivateClick = viewModel::activate,
        onActivationSuccess = onActivationSuccess
    )
}

@Composable
fun ActivationContent(
    uiState: ActivationUiState,
    onTerminalIdChange: (String) -> Unit,
    onMerchantIdChange: (String) -> Unit,
    onActivationCodeChange: (String) -> Unit,
    onActivateClick: () -> Unit,
    onActivationSuccess: () -> Unit
) {
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onActivationSuccess()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.title_terminal_activation),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = uiState.terminalId,
                onValueChange = onTerminalIdChange,
                label = { Text(stringResource(R.string.label_terminal_id)) },
                placeholder = { Text(stringResource(R.string.hint_terminal_id)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !uiState.isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.merchantId,
                onValueChange = onMerchantIdChange,
                label = { Text(stringResource(R.string.label_merchant_id)) },
                placeholder = { Text(stringResource(R.string.hint_merchant_id)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !uiState.isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.activationCode,
                onValueChange = onActivationCodeChange,
                label = { Text(stringResource(R.string.label_activation_code)) },
                placeholder = { Text(stringResource(R.string.hint_activation_code)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                enabled = !uiState.isLoading
            )

            if (uiState.error != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = uiState.error,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onActivateClick,
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                enabled = !uiState.isLoading
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(stringResource(R.string.label_activate_terminal))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ActivationPreview() {
    MiniEDCMerchantAppTheme {
        ActivationContent(
            uiState = ActivationUiState(
                terminalId = stringResource(R.string.dummy_terminal_id),
                merchantId = stringResource(R.string.dummy_merchant_id)
            ),
            onTerminalIdChange = {},
            onMerchantIdChange = {},
            onActivationCodeChange = {},
            onActivateClick = {},
            onActivationSuccess = {}
        )
    }
}
