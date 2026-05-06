package id.faradyna.miniedcapp.domain.service

import id.faradyna.miniedcapp.domain.model.PrintResult
import id.faradyna.miniedcapp.domain.model.Receipt

/**
 * Interface untuk mencetak struk
 */
interface PrinterService {
    suspend fun print(receipt: Receipt): PrintResult
}
