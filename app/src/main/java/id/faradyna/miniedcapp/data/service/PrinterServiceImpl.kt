package id.faradyna.miniedcapp.data.service

import android.util.Log
import id.faradyna.miniedcapp.domain.model.PrintResult
import id.faradyna.miniedcapp.domain.model.Receipt
import id.faradyna.miniedcapp.domain.service.PrinterService
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementasi mock dari PrinterService.
 *
 * Mensimulasikan proses cetak pada printer:
 * - Delay untuk meniru proses cetak fisik
 * - Format struk ke layout teks sederhana (mirip printer asli)
 * - Simulasi gagal print (misalnya kehabisan kertas)
 */
@Singleton
class PrinterServiceImpl @Inject constructor() : PrinterService {
    override suspend fun print(receipt: Receipt): PrintResult {
        Log.d("MockPrinter", "Starting print job...")
        delay(2000)

        if (receipt.traceNumber.endsWith("9")) {
            Log.e("MockPrinter", "Printer Error: Out of Paper")
            return PrintResult.
            Error("Printer Error: Out of Paper")
        }
        
        val receiptContent = """
            ===========================
            ${receipt.merchantName}
            TID: ${receipt.terminalId}
            ===========================
            DATE: ${receipt.dateTime}
            TRACE: ${receipt.traceNumber}
            RRN: ${receipt.rrn}
            APP CODE: ${receipt.approvalCode}
            ---------------------------
            AMOUNT: ${receipt.amount}
            STATUS: ${receipt.status}
            ===========================
            THANK YOU
            ===========================
        """.trimIndent()
        
        Log.d("MockPrinter", "\n$receiptContent")
        return PrintResult.Success
    }
}
