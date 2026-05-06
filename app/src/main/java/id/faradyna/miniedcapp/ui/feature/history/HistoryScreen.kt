package id.faradyna.miniedcapp.ui.feature.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import id.faradyna.miniedcapp.R
import id.faradyna.miniedcapp.domain.model.SaleTransaction
import id.faradyna.miniedcapp.domain.model.TransactionStatus
import id.faradyna.miniedcapp.ui.feature.sale.ReceiptView
import id.faradyna.miniedcapp.ui.feature.sale.formatCurrency
import id.faradyna.miniedcapp.ui.theme.MiniEDCMerchantAppTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    onBack: () -> Unit,
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.title_transaction_history)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (uiState.transactions.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(stringResource(R.string.label_no_transactions_found))
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                items(uiState.transactions) { transaction ->
                    TransactionItem(
                        transaction = transaction,
                        onClick = { viewModel.selectTransaction(transaction) }
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                }
            }
        }

        if (uiState.selectedTransaction != null) {
            TransactionDetailDialog(
                uiState = uiState,
                onPrint = viewModel::rePrint,
                onRetry = {
                    uiState.selectedTransaction?.let { viewModel.retryTransaction(it) }
                },
                onDismiss = { viewModel.selectTransaction(null) }
            )
        }
    }
}

@Composable
fun TransactionItem(
    transaction: SaleTransaction,
    onClick: () -> Unit
) {
    val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    val dateString = sdf.format(Date(transaction.timestamp))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = stringResource(
                    R.string.value_trace_no_string,
                    transaction.traceNumber
                ),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = dateString,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = formatCurrency(transaction.amount),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = transaction.status.name,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = when (transaction.status) {
                    TransactionStatus.SUCCESS -> Color(0xFF4CAF50)
                    TransactionStatus.FAILED -> MaterialTheme.colorScheme.error
                    TransactionStatus.PENDING -> Color(0xFFFF9800)
                }
            )
        }
    }
}

@Composable
fun TransactionDetailDialog(
    uiState: HistoryUiState,
    onPrint: () -> Unit,
    onRetry: () -> Unit,
    onDismiss: () -> Unit
) {
    val transaction = uiState.selectedTransaction ?: return
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())

    AlertDialog(
        onDismissRequest = if (uiState.isPrinting) ({}) else onDismiss,
        confirmButton = {
            when (transaction.status) {
                TransactionStatus.SUCCESS -> {
                    Button(
                        onClick = onPrint,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !uiState.isPrinting
                    ) {
                        if (uiState.isPrinting) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(stringResource(R.string.label_printing))
                        } else {
                            Text(stringResource(R.string.label_re_print_receipt))
                        }
                    }
                }

                TransactionStatus.PENDING -> {
                    Button(
                        onClick = onRetry,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !uiState.isRetrying
                    ) {
                        if (uiState.isRetrying) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(stringResource(R.string.label_retrying))
                        } else {
                            Text(stringResource(R.string.label_retry_sync_transaction))
                        }
                    }
                }

                TransactionStatus.FAILED -> {
                    Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                        Text(stringResource(R.string.label_close))
                    }
                }
            }
        },
        dismissButton = {
            if (transaction.status != TransactionStatus.FAILED) {
                TextButton(onClick = onDismiss, enabled = !uiState.isPrinting) {
                    Text(stringResource(R.string.label_close))
                }
            }
        },
        title = {
            Text(
                text = stringResource(R.string.title_transaction_detail),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                DetailRow(stringResource(R.string.label_merchant), uiState.merchantName)
                DetailRow(stringResource(R.string.label_merchant_id), uiState.merchantId)
                DetailRow(stringResource(R.string.label_terminal_id), uiState.terminalId)
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                DetailRow(stringResource(R.string.label_amount), formatCurrency(transaction.amount))
                DetailRow(stringResource(R.string.label_status), transaction.status.name)
                DetailRow(stringResource(R.string.label_message), transaction.message ?: "-")
                DetailRow(stringResource(R.string.label_trace_no), transaction.traceNumber)
                DetailRow(stringResource(R.string.label_invoice_no), transaction.invoiceNumber)
                DetailRow(stringResource(R.string.label_rrn), transaction.rrn ?: "-")
                DetailRow("App Code", transaction.approvalCode ?: "-")
                DetailRow(stringResource(R.string.label_timestamp), sdf.format(Date(transaction.timestamp)))

                if (transaction.status == TransactionStatus.SUCCESS && uiState.receiptPreview != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.label_receipt_preview),
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Card(
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        ReceiptView(receipt = uiState.receiptPreview)
                    }
                }
            }
        }
    )
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HistoryPreview() {
    MiniEDCMerchantAppTheme {
        HistoryScreen(onBack = {})
    }
}
