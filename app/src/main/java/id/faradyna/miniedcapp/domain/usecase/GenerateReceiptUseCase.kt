package id.faradyna.miniedcapp.domain.usecase

import id.faradyna.miniedcapp.domain.model.Receipt
import id.faradyna.miniedcapp.domain.model.SaleTransaction
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * UseCase untuk membuat data struk (receipt) dari hasil transaksi.
 *
 * Bertanggung jawab untuk:
 * - Format tanggal & waktu sesuai lokal device
 * - Format nominal ke mata uang (IDR)
 * - Menggabungkan data transaksi, merchant, dan terminal menjadi model Receipt
 */
class GenerateReceiptUseCase @Inject constructor() {
    operator fun invoke(
        saleTransaction: SaleTransaction,
        merchantName: String,
        terminalId: String
    ): Receipt {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        val dateString = sdf.format(Date(saleTransaction.timestamp))
        
        val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        val amountString = format.format(saleTransaction.amount)
        
        return Receipt(
            merchantName = merchantName,
            terminalId = terminalId,
            traceNumber = saleTransaction.traceNumber,
            dateTime = dateString,
            amount = amountString,
            status = saleTransaction.status.name,
            rrn = saleTransaction.rrn ?: "-",
            approvalCode = saleTransaction.approvalCode ?: "-"
        )
    }
}
