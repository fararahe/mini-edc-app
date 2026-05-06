package id.faradyna.miniedcapp.ui.feature.sale

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import id.faradyna.miniedcapp.R
import id.faradyna.miniedcapp.domain.model.Receipt
import id.faradyna.miniedcapp.domain.model.SaleTransaction
import id.faradyna.miniedcapp.domain.model.TransactionStatus
import id.faradyna.miniedcapp.ui.theme.MiniEDCMerchantAppTheme
import java.text.NumberFormat
import java.util.*

@Composable
fun SaleScreen(
    onBack: () -> Unit,
    viewModel: SaleViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    SaleContent(
        uiState = uiState,
        onAmountChange = viewModel::onAmountChange,
        onProcessSale = viewModel::processSale,
        onPrintReceipt = viewModel::printReceipt,
        onBack = onBack,
        onDismissResult = {
            viewModel.resetState()
            if (uiState.transactionResult?.status == TransactionStatus.SUCCESS) {
                onBack()
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaleContent(
    uiState: SaleUiState,
    onAmountChange: (String) -> Unit,
    onProcessSale: () -> Unit,
    onPrintReceipt: () -> Unit,
    onBack: () -> Unit,
    onDismissResult: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.title_sale_transaction)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.label_enter_amount),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = formatCurrency(uiState.amount.toLongOrNull() ?: 0L),
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(32.dp))

                OutlinedTextField(
                    value = uiState.amount,
                    onValueChange = onAmountChange,
                    label = { Text(stringResource(R.string.label_amount)) },
                    placeholder = { Text(stringResource(R.string.hint_amount)) },
                    prefix = { Text(stringResource(R.string.label_currency)) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    enabled = !uiState.isLoading
                )

                if (uiState.error != null) {
                    Text(
                        text = uiState.error,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = onProcessSale,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = !uiState.isLoading && uiState.amount.isNotEmpty(),
                    shape = MaterialTheme.shapes.medium
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(stringResource(R.string.label_process_payment))
                    }
                }
            }

            if (uiState.transactionResult != null) {
                TransactionResultDialog(
                    transaction = uiState.transactionResult,
                    receipt = uiState.receipt,
                    isPrinting = uiState.isPrinting,
                    onPrint = onPrintReceipt,
                    onDismiss = onDismissResult
                )
            }
        }
    }
}

@Composable
fun TransactionResultDialog(
    transaction: SaleTransaction,
    receipt: Receipt?,
    isPrinting: Boolean,
    onPrint: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = if (isPrinting) ({}) else onDismiss,
        confirmButton = {
            if (transaction.status == TransactionStatus.SUCCESS) {
                Button(
                    onClick = onPrint,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isPrinting
                ) {
                    if (isPrinting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(stringResource(R.string.label_printing))
                    } else {
                        Text(stringResource(R.string.label_print_receipt))
                    }
                }
            } else {
                Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                    Text(stringResource(R.string.label_close))
                }
            }
        },
        dismissButton = {
            if (transaction.status == TransactionStatus.SUCCESS) {
                TextButton(onClick = onDismiss, enabled = !isPrinting) {
                    Text(stringResource(R.string.label_skip))
                }
            }
        },
        title = {
            Text(
                text = stringResource(R.string.value_transaction_string, transaction.status),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = when (transaction.status) {
                    TransactionStatus.SUCCESS -> Color(0xFF4CAF50)
                    TransactionStatus.FAILED -> MaterialTheme.colorScheme.error
                    TransactionStatus.PENDING -> Color(0xFFFF9800)
                }
            )
        },
        text = {
            if (transaction.status == TransactionStatus.SUCCESS && receipt != null) {
                ReceiptView(receipt)
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(stringResource(R.string.value_trace_no_string, transaction.traceNumber))
                    Text(
                        stringResource(
                            R.string.value_amount_string,
                            formatCurrency(transaction.amount)
                        )
                    )
                    if (transaction.status == TransactionStatus.PENDING) {
                        Text(
                            stringResource(R.string.hint_check_payment_status),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun ReceiptView(receipt: Receipt) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(R.string.title_mini_edc_merchant), fontWeight = FontWeight.Bold)
        Text(receipt.merchantName, fontSize = 12.sp)
        Text(stringResource(R.string.divider), fontFamily = FontFamily.Monospace)

        ReceiptRow(stringResource(R.string.label_date), receipt.dateTime)
        ReceiptRow(stringResource(R.string.label_tid), receipt.terminalId)
        ReceiptRow(stringResource(R.string.label_trace), receipt.traceNumber)
        ReceiptRow(stringResource(R.string.label_rrn), receipt.rrn)
        ReceiptRow(stringResource(R.string.label_app_code), receipt.approvalCode)

        Text(stringResource(R.string.divider), fontFamily = FontFamily.Monospace)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(stringResource(R.string.label_total), fontWeight = FontWeight.Bold)
            Text(receipt.amount, fontWeight = FontWeight.Bold)
        }

        Text(stringResource(R.string.divider), fontFamily = FontFamily.Monospace)
        Text(
            stringResource(R.string.value_transaction_string, receipt.status).uppercase(),
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(stringResource(R.string.label_mock_printer_output), fontSize = 10.sp)
    }
}

@Composable
fun ReceiptRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
        Text(value, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
    }
}

fun formatCurrency(amount: Long): String {
    val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    return format.format(amount)
}

@Preview(showBackground = true)
@Composable
fun SalePreview() {
    MiniEDCMerchantAppTheme {
        SaleContent(
            uiState = SaleUiState(amount = "150000"),
            onAmountChange = {},
            onProcessSale = {},
            onPrintReceipt = {},
            onBack = {},
            onDismissResult = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ReceiptPreview() {
    MiniEDCMerchantAppTheme {
        ReceiptView(
            receipt = Receipt(
                merchantName = "TOKO MAJU JAYA",
                terminalId = "EDC-0001",
                traceNumber = "000001",
                dateTime = "01/01/2024 10:00:00",
                amount = "Rp 150.000,00",
                status = "SUCCESS",
                rrn = "RRN12345678",
                approvalCode = "123456"
            )
        )
    }
}
