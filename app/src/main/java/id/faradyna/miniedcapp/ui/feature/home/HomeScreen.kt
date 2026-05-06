package id.faradyna.miniedcapp.ui.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import id.faradyna.miniedcapp.R
import id.faradyna.miniedcapp.ui.main.MainUiState
import id.faradyna.miniedcapp.ui.main.MainViewModel
import id.faradyna.miniedcapp.ui.theme.MiniEDCMerchantAppTheme

@Composable
fun HomeScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    HomeContent(
        uiState = uiState,
        onLogoutClick = viewModel::logout,
    )

}

@Composable
fun HomeContent(
    uiState: MainUiState,
    onLogoutClick: () -> Unit,
) {
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
                text = stringResource(R.string.title_terminal_activated),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    InfoRow(label = stringResource(R.string.label_merchant), value = uiState.merchantName ?: "-")
                    InfoRow(label = stringResource(R.string.label_terminal_id), value = uiState.terminalId ?: "-")
                    InfoRow(label = stringResource(R.string.label_merchant_id), value = uiState.merchantId ?: "-")
                    InfoRow(label = stringResource(R.string.label_device_sn), value = uiState.deviceSn)
                    InfoRow(label = stringResource(R.string.label_model), value = uiState.deviceModel)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            TextButton(
                onClick = onLogoutClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Deactivate Terminal", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    MiniEDCMerchantAppTheme {
        HomeContent(
            uiState = MainUiState(
                isActivated = true,
                merchantName = "Toko Maju Jaya",
                terminalId = "EDC-0001"
            ),
            onLogoutClick = {},
        )
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontWeight = FontWeight.Medium)
        Text(text = value)
    }
}
